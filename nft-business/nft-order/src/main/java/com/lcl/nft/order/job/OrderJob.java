package com.lcl.nft.order.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcl.nft.api.common.constant.BizOrderType;
import com.lcl.nft.api.common.constant.BusinessCode;
import com.lcl.nft.api.order.OrderFacadeService;
import com.lcl.nft.api.order.request.OrderConfirmRequest;
import com.lcl.nft.api.order.request.OrderTimeoutRequest;
import com.lcl.nft.api.pay.constant.PayOrderState;
import com.lcl.nft.api.pay.model.PayOrderVO;
import com.lcl.nft.api.pay.request.PayQueryByBizNo;
import com.lcl.nft.api.pay.request.PayQueryRequest;
import com.lcl.nft.api.pay.service.PayFacadeService;
import com.lcl.nft.api.user.constant.UserType;
import com.lcl.nft.base.response.MultiResponse;
import com.lcl.nft.order.domain.entity.TradeOrder;
import com.lcl.nft.order.domain.service.OrderReadService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author conglongli
 * @date 2025/3/22 23:53
 */
@Component
public class OrderJob {
    @Autowired
    private OrderFacadeService orderFacadeService;

    @Autowired
    private OrderReadService orderReadService;

    @Autowired
    private PayFacadeService payFacadeService;

    private static final int CAPACITY = 2000;

    private final BlockingQueue<TradeOrder> orderConfirmBlockingQueue = new LinkedBlockingQueue<>(CAPACITY);

    private final BlockingQueue<TradeOrder> orderTimeoutBlockingQueue = new LinkedBlockingQueue<>(CAPACITY);

    /**
     * todo 使用动态线程池
     */
    private final ForkJoinPool forkJoinPool = new ForkJoinPool(10);

    private static final int PAGE_SIZE = 500;

    private static final Logger LOG = LoggerFactory.getLogger(OrderJob.class);

    private static final TradeOrder POISON = new TradeOrder();

    private static int MAX_TAIL_NUMBER = 99;


    /**
     * 订单失效定时任务（使用MQ）
     * @return
     */
    @XxlJob("orderTimeOutExecute")
    public ReturnT<String> orderTimeOutExecute() {
        try {
            int shardIndex = XxlJobHelper.getShardIndex();
            int shardTotal = XxlJobHelper.getShardTotal();

            LOG.info("orderTimeOutExecute start to execute , shardIndex is {} , shardTotal is {}", shardIndex, shardTotal);

            List<String> buyerIdTailNumberList = new ArrayList<>();
            for (int i = 0; i <= MAX_TAIL_NUMBER; i++) {
                if (i % shardTotal == shardIndex) {
                    buyerIdTailNumberList.add(StringUtils.leftPad(String.valueOf(i), 2, "0"));
                }
            }

            buyerIdTailNumberList.forEach(buyerIdTailNumber -> {
                try {
                    int currentPage = 1;
                    Page<TradeOrder> page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                    //其实这里用put更好一点，可以避免因为队列满了而导致异常而提前结束。
                    orderTimeoutBlockingQueue.addAll(page.getRecords());
                    forkJoinPool.execute(this::executeTimeout);

                    while (page.hasNext()) {
                        currentPage++;
                        page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                        orderTimeoutBlockingQueue.addAll(page.getRecords());
                    }
                } finally {
                    orderTimeoutBlockingQueue.add(POISON);
                    LOG.debug("POISON added to blocking queue ，buyerIdTailNumber is {}", buyerIdTailNumber);
                }
            });

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            LOG.error("orderTimeOutExecute failed", e);
            throw e;
        }
    }

    /**
     * 订单确认定时任务（使用MQ）
     * @return
     */
    @XxlJob("orderConfirmExecute")
    public ReturnT<String> orderConfirmExecute() {

        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        LOG.info("orderConfirmExecute start to execute , shardIndex is {} , shardTotal is {}", shardIndex, shardTotal);

        List<String> buyerIdTailNumberList = new ArrayList<>();
        for (int i = 0; i <= MAX_TAIL_NUMBER; i++) {
            if (i % shardTotal == shardIndex) {
                buyerIdTailNumberList.add(StringUtils.leftPad(String.valueOf(i), 2, "0"));
            }
        }

        buyerIdTailNumberList.forEach(buyerIdTailNumber -> {
            try {
                int currentPage = 1;
                Page<TradeOrder> page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                orderConfirmBlockingQueue.addAll(page.getRecords());
                forkJoinPool.execute(this::executeConfirm);

                while (page.hasNext()) {
                    currentPage++;
                    page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, buyerIdTailNumber);
                    orderConfirmBlockingQueue.addAll(page.getRecords());
                }
            } finally {
                orderConfirmBlockingQueue.add(POISON);
                LOG.debug("POISON added to blocking queue ，buyerIdTailNumber is {}", buyerIdTailNumber);
            }
        });

        return ReturnT.SUCCESS;
    }

    /**
     * 订单确认
     */
    private void executeConfirm() {
        TradeOrder tradeOrder = null;
        try {
            while (true) {
                tradeOrder = orderConfirmBlockingQueue.take();
                if (tradeOrder == POISON) {
                    LOG.debug("POISON toked from blocking queue");
                    break;
                }
                executeConfirmSingle(tradeOrder);
            }
        } catch (InterruptedException e) {
            LOG.error("executeConfirm failed", e);
        }
        LOG.debug("executeConfirm finish");
    }

    /**
     * 订单失效
     */
    private void executeTimeout() {
        TradeOrder tradeOrder = null;
        try {
            while (true) {
                tradeOrder = orderTimeoutBlockingQueue.take();
                if (tradeOrder == POISON) {
                    LOG.debug("POISON toked from blocking queue");
                    break;
                }
                LOG.info("executeTimeout tradeOrderId = {}" , tradeOrder.getId());
                executeTimeoutSingle(tradeOrder);
            }
        } catch (InterruptedException e) {
            LOG.error("executeTimeout failed", e);
        }
        LOG.debug("executeTimeout finish");
    }

    /**
     * 订单失效定时任务（使用 Hint）
     * @return
     */
    @XxlJob("orderTimeOutExecuteWithHint")
    @Deprecated
    public ReturnT<String> orderTimeOutExecuteWithHint() {
        try {
            int shardIndex = XxlJobHelper.getShardIndex();
            int shardTotal = XxlJobHelper.getShardTotal();

            LOG.info("orderTimeOutExecute start to execute , shardIndex is {} , shardTotal is {}", shardIndex, shardTotal);

            int shardingTableCount = BusinessCode.TRADE_ORDER.tableCount();

            if (shardIndex >= shardingTableCount) {
                return ReturnT.SUCCESS;
            }

            List<Integer> shardingTableIndexes = new ArrayList<>();
            for (int realTableIndex = 0; realTableIndex < shardingTableCount; realTableIndex++) {
                if (realTableIndex % shardTotal == shardIndex) {
                    shardingTableIndexes.add(realTableIndex);
                }
            }

            shardingTableIndexes.forEach(index -> {

                try (HintManager hintManager = HintManager.getInstance()) {
                    LOG.info("shardIndex {} is execute", index);
                    hintManager.addTableShardingValue("trade_order", "000" + index);
                    int currentPage = 1;
                    Page<TradeOrder> page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, null);
                    page.getRecords().forEach(this::executeTimeoutSingle);
                    while (page.hasNext()) {
                        currentPage++;
                        page = orderReadService.pageQueryTimeoutOrders(currentPage, PAGE_SIZE, null);
                        page.getRecords().forEach(this::executeTimeoutSingle);
                    }
                }
            });

            return ReturnT.SUCCESS;
        } catch (Exception e) {
            LOG.error("orderTimeOutExecute failed", e);
            throw e;
        }
    }

    /**
     * 订单确认定时任务（使用 Hint）
     * @return
     */
    @XxlJob("orderConfirmExecuteWithHint")
    @Deprecated
    public ReturnT<String> orderConfirmExecuteWithHint() {

        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        int shardingTableCount = BusinessCode.TRADE_ORDER.tableCount();

        if (shardIndex >= shardingTableCount) {
            return ReturnT.SUCCESS;
        }

        List<Integer> shardingTableIndexes = new ArrayList<>();
        for (int realTableIndex = 0; realTableIndex < shardingTableCount; realTableIndex++) {
            if (realTableIndex % shardTotal == shardIndex) {
                shardingTableIndexes.add(realTableIndex);
            }
        }

        shardingTableIndexes.parallelStream().forEach(index -> {
            HintManager hintManager = HintManager.getInstance();
            hintManager.addTableShardingValue("trade_order", "000" + index);
            int currentPage = 1;
            Page<TradeOrder> page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, null);
            page.getRecords().forEach(this::executeConfirmSingle);
            while (page.hasNext()) {
                currentPage++;
                page = orderReadService.pageQueryNeedConfirmOrders(currentPage, PAGE_SIZE, null);
                page.getRecords().forEach(this::executeConfirmSingle);
            }
        });

        return ReturnT.SUCCESS;
    }

    /**
     * 单条处理订单失效
     * @param tradeOrder
     */
    private void executeTimeoutSingle(TradeOrder tradeOrder) {
        // 查询支付单，判断是否已经支付成功。
        PayQueryRequest request = new PayQueryRequest();
        request.setPayerId(tradeOrder.getBuyerId());
        request.setPayOrderState(PayOrderState.PAID);
        PayQueryByBizNo payQueryByBizNo = new PayQueryByBizNo();
        payQueryByBizNo.setBizNo(tradeOrder.getOrderId());
        payQueryByBizNo.setBizType(BizOrderType.TRADE_ORDER.name());
        request.setPayQueryCondition(payQueryByBizNo);
        MultiResponse<PayOrderVO> payQueryResponse = payFacadeService.queryPayOrders(request);

        // 如果没有支付，则进行失效处理
        if (payQueryResponse.getSuccess() && CollectionUtils.isEmpty(payQueryResponse.getDatas())) {
            LOG.info("start to execute order timeout , orderId is {}", tradeOrder.getOrderId());
            OrderTimeoutRequest orderTimeoutRequest = new OrderTimeoutRequest();
            orderTimeoutRequest.setOrderId(tradeOrder.getOrderId());
            orderTimeoutRequest.setOperateTime(new Date());
            orderTimeoutRequest.setOperator(UserType.PLATFORM.name());
            orderTimeoutRequest.setOperatorType(UserType.PLATFORM);
            orderTimeoutRequest.setIdentifier(tradeOrder.getOrderId());
            orderFacadeService.timeout(orderTimeoutRequest);
        }
    }


    /**
     * 单条订单确认
     * @param tradeOrder
     */
    private void executeConfirmSingle(TradeOrder tradeOrder) {
        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setOperator(UserType.PLATFORM.name());
        confirmRequest.setOperatorType(UserType.PLATFORM);
        confirmRequest.setOrderId(tradeOrder.getOrderId());
        confirmRequest.setIdentifier(tradeOrder.getIdentifier());
        confirmRequest.setOperateTime(new Date());
        confirmRequest.setOrderId(tradeOrder.getOrderId());
        confirmRequest.setBuyerId(tradeOrder.getBuyerId());
        confirmRequest.setItemCount((long)tradeOrder.getItemCount());

        orderFacadeService.confirm(confirmRequest);
    }
}
