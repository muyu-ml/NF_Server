package com.lcl.nft.order.domain.service;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcl.nft.api.order.constant.OrderErrorCode;
import com.lcl.nft.api.order.constant.TradeOrderEvent;
import com.lcl.nft.api.order.request.*;
import com.lcl.nft.api.order.response.OrderResponse;
import com.lcl.nft.api.user.constant.UserType;
import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.RepoErrorCode;
import com.lcl.nft.base.utils.BeanValidator;
import com.lcl.nft.order.domain.entity.TradeOrder;
import com.lcl.nft.order.domain.entity.TradeOrderStream;
import com.lcl.nft.order.domain.exception.OrderException;
import com.lcl.nft.order.domain.listener.event.OrderCreateEvent;
import com.lcl.nft.order.infrastructure.mapper.OrderMapper;
import com.lcl.nft.order.infrastructure.mapper.OrderStreamMapper;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.lcl.nft.api.order.constant.OrderErrorCode.ORDER_NOT_EXIST;
import static com.lcl.nft.api.order.constant.OrderErrorCode.PERMISSION_DENIED;
import static com.lcl.nft.base.response.ResponseCode.SYSTEM_ERROR;
import static java.util.Objects.requireNonNull;

/**
 * @Author conglongli
 * @date 2025/3/16 11:00
 */
@Service
public class OrderManageService extends ServiceImpl<OrderMapper, TradeOrder> {

    private static final Logger logger = LoggerFactory.getLogger(OrderManageService.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStreamMapper orderStreamMapper;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Autowired
    protected ApplicationContext applicationContext;


    /**
     * 创建订单
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse create(OrderCreateRequest request) {
        // 幂等判断
        TradeOrder existsOrder = orderMapper.selectByIdentifier(request.getIdentifier(), request.getBuyerId());
        if(existsOrder != null) {
            return new OrderResponse.OrderResponseBuilder().orderId(existsOrder.getOrderId()).buildSuccess();
        }

        // 保存订单信息与订单操作流水信息
        TradeOrder tradeOrder = TradeOrder.createOrder(request);
        boolean result = save(tradeOrder);
        Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));

        TradeOrderStream tradeOrderStream = new TradeOrderStream(tradeOrder, request.getOrderEvent(), request.getIdentifier());
        result = orderStreamMapper.insert(tradeOrderStream) == 1;
        Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));

        // 发布订单创建事件
        applicationContext.publishEvent(new OrderCreateEvent(tradeOrder));
        return new OrderResponse.OrderResponseBuilder().orderId(tradeOrder.getOrderId()).buildSuccess();
    }


    /**
     * 订单创建并确认
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse createAndConfirm(OrderCreateAndConfirmRequest request) {
        // 幂等判断
        TradeOrder existOrder = orderMapper.selectByIdentifier(request.getIdentifier(), request.getBuyerId());
        if (existOrder != null) {
            return new OrderResponse.OrderResponseBuilder().orderId(existOrder.getOrderId()).buildSuccess();
        }

        // 保存订单信息与订单操作流水信息
        TradeOrder tradeOrder = TradeOrder.createOrder(request);
        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        BeanUtils.copyProperties(request, orderConfirmRequest);
        orderConfirmRequest.setOrderId(request.getOrderId());
        tradeOrder.confirm(orderConfirmRequest);
        boolean result = save(tradeOrder);
        Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));
        TradeOrderStream orderStream = new TradeOrderStream(tradeOrder, request.getOrderEvent(), request.getIdentifier());
        result = orderStreamMapper.insert(orderStream) == 1;
        Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));

        return new OrderResponse.OrderResponseBuilder().orderId(tradeOrder.getOrderId()).buildSuccess();
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingSphereTransactionType(TransactionType.BASE)
    public OrderResponse pay(OrderPayRequest request) {
        return doExecuteWithoutTrans(request, tradeOrder -> tradeOrder.pay(request));
    }

    /**
     * 订单确认
     *
     * @param request
     * @return
     */
    public OrderResponse confirm(OrderConfirmRequest request) {
        return doExecute(request, tradeOrder -> tradeOrder.confirm(request));
    }

    /**
     * 订单取消
     *
     * @param request
     * @return
     */
    public OrderResponse cancel(OrderCancelRequest request) {
        return doExecute(request, tradeOrder -> tradeOrder.close(request));
    }

    /**
     * 订单超时
     *
     * @param request
     * @return
     */
    public OrderResponse timeout(OrderTimeoutRequest request) {
        return doExecute(request, tradeOrder -> tradeOrder.close(request));
    }

    /**
     * 订单完结
     *
     * @param request
     * @return
     */
    public OrderResponse finish(OrderFinishRequest request) {
        return doExecute(request, tradeOrder -> tradeOrder.finish(request));
    }



    /**
     * 通用订单更新逻辑--策略模式
     *
     * @param orderRequest
     * @param consumer
     * @return
     */
    protected OrderResponse doExecute(BaseOrderUpdateRequest orderRequest, Consumer<TradeOrder> consumer) {
        OrderResponse response = new OrderResponse();
        return handle(orderRequest, response, "doExecute", request -> {

            // 通用操作前的验证：订单需要已存在，本次订单操作记录不存在，切当前操作人有人操作权限
            TradeOrder existOrder = orderMapper.selectByOrderId(request.getOrderId());
            if (existOrder == null) {
                throw new OrderException(ORDER_NOT_EXIST);
            }

            if (!hasPermission(existOrder, orderRequest.getOrderEvent(), orderRequest.getOperator(), orderRequest.getOperatorType())) {
                throw new OrderException(PERMISSION_DENIED);
            }

            TradeOrderStream existStream = orderStreamMapper.selectByIdentifier(orderRequest.getIdentifier(), orderRequest.getOrderEvent().name(), orderRequest.getOrderId());
            if (existStream != null) {
                return new OrderResponse.OrderResponseBuilder().orderId(existStream.getOrderId()).streamId(existStream.getId().toString()).buildDuplicated();
            }

            // 核心逻辑执行
            consumer.accept(existOrder);

            // 更新订单信息，新增操作流水（使用事务模板处理事务）
            return transactionTemplate.execute(transactionStatus -> {
                boolean result = orderMapper.updateByOrderId(existOrder) == 1;
                Assert.isTrue(result, () -> new OrderException(OrderErrorCode.UPDATE_ORDER_FAILED));

                TradeOrderStream orderStream = new TradeOrderStream(existOrder, orderRequest.getOrderEvent(), orderRequest.getIdentifier());
                result = orderStreamMapper.insert(orderStream) == 1;
                Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));

                return new OrderResponse.OrderResponseBuilder().orderId(orderStream.getOrderId()).streamId(String.valueOf(orderStream.getId())).buildSuccess();
            });
        });
    }


    /**
     * 通用订单更新逻辑（不带事务，需要调用方自己保证事务）
     * @param orderRequest
     * @param consumer
     * @return
     */
    private OrderResponse doExecuteWithoutTrans(BaseOrderUpdateRequest orderRequest, Consumer<TradeOrder> consumer) {
        OrderResponse response = new OrderResponse();
        return handle(orderRequest, response, "doExecute", request -> {

            // 通用操作前的验证：订单需要已存在，本次订单操作记录不存在，切当前操作人有人操作权限
            TradeOrder existOrder = orderMapper.selectByOrderId(request.getOrderId());
            if (existOrder == null) {
                throw new OrderException(ORDER_NOT_EXIST);
            }

            if (!hasPermission(existOrder, orderRequest.getOrderEvent(), orderRequest.getOperator(), orderRequest.getOperatorType())) {
                throw new OrderException(PERMISSION_DENIED);
            }

            TradeOrderStream existStream = orderStreamMapper.selectByIdentifier(orderRequest.getIdentifier(), orderRequest.getOrderEvent().name(), orderRequest.getOrderId());
            if(existStream != null) {
                return new OrderResponse.OrderResponseBuilder().orderId(existStream.getOrderId()).streamId(existStream.getOrderId()).buildDuplicated();
            }

            // 核心逻辑执行
            consumer.accept(existOrder);

            // 更新订单信息，新增操作流水
            boolean result = orderMapper.updateByOrderId(existOrder) == 1;
            Assert.isTrue(result, () -> new OrderException(OrderErrorCode.UPDATE_ORDER_FAILED));

            TradeOrderStream orderStream = new TradeOrderStream(existOrder, orderRequest.getOrderEvent(), orderRequest.getIdentifier());
            result = orderStreamMapper.insert(orderStream) == 1;
            Assert.isTrue(result, () -> new BizException(RepoErrorCode.INSERT_FAILED));

            return new OrderResponse.OrderResponseBuilder().orderId(orderStream.getOrderId()).streamId(String.valueOf(orderStream.getId())).buildSuccess();
        });
    }

    /**
     * 判断用户是否有权限处理
     * @param existOrder
     * @param orderEvent
     * @param operator
     * @param operatorType
     * @return
     */
    private boolean hasPermission(TradeOrder existOrder, TradeOrderEvent orderEvent, String operator, UserType operatorType) {
        switch (orderEvent) {
            case PAY:
            case CANCEL:
                return existOrder.getBuyerId().equals(operator);
            case TIME_OUT:
            case CONFIRM:
            case FINISH:
                return operatorType == UserType.PLATFORM;
            default:
                throw new UnsupportedOperationException("unsupport order event : " + orderEvent);
        }
    }


    /**
     * 订单请求的通用流程 -- 模板模式
     * @param request
     * @param response
     * @param method
     * @param function
     * @return
     * @param <T>
     * @param <R>
     */
    public static <T, R extends OrderResponse> OrderResponse handle(T request, R response, String method, Function<T, R> function) {
        logger.info("before execute method={}, request={}", method, JSON.toJSONString(request));
        try {
            requireNonNull(request);
            BeanValidator.validateObject(request);
            response = function.apply(request);
        } catch (OrderException e) {
            logger.error(e.toString(), e);
            response.setSuccess(false);
            response.setResponseCode(e.getErrorCode().getCode());
            response.setResponseMessage(e.getErrorCode().getMessage());
            logger.error("failed execute method={}, exception={}", method, JSON.toJSONString(e));
        } catch (Exception e) {
            response.setSuccess(false);
            response.setResponseCode(SYSTEM_ERROR.name());
            response.setResponseMessage(e.getMessage());
            logger.error("failed execute method={}, exception={}", method, JSON.toJSONString(e));
        } finally {
            logger.info("after execute method={}, result={}", method, JSON.toJSONString(response));
        }
        return response;
    }

}
