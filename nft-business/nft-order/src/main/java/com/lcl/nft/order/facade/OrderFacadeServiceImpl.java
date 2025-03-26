package com.lcl.nft.order.facade;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcl.nft.api.collection.request.CollectionSaleRequest;
import com.lcl.nft.api.collection.response.CollectionSaleResponse;
import com.lcl.nft.api.collection.service.CollectionFacadeService;
import com.lcl.nft.api.order.OrderFacadeService;
import com.lcl.nft.api.order.constant.OrderErrorCode;
import com.lcl.nft.api.order.model.TradeOrderVO;
import com.lcl.nft.api.order.request.*;
import com.lcl.nft.api.order.response.OrderResponse;
import com.lcl.nft.api.user.constant.UserType;
import com.lcl.nft.api.user.request.UserQueryRequest;
import com.lcl.nft.api.user.response.UserQueryResponse;
import com.lcl.nft.api.user.response.data.UserInfo;
import com.lcl.nft.api.user.service.UserFacadeService;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.base.response.SingleResponse;
import com.lcl.nft.lock.DistributeLock;
import com.lcl.nft.mq.producer.StreamProducer;
import com.lcl.nft.order.domain.entity.TradeOrder;
import com.lcl.nft.order.domain.entity.convertor.TradeOrderConvertor;
import com.lcl.nft.order.domain.exception.OrderException;
import com.lcl.nft.order.domain.service.OrderManageService;
import com.lcl.nft.order.domain.service.OrderReadService;
import com.lcl.nft.order.domain.validator.OrderCreateValidator;
import com.lcl.nft.order.wrapper.InventoryWrapperService;
import com.lcl.nft.rpc.facade.Facade;
import jakarta.validation.constraints.NotNull;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.lcl.nft.api.order.constant.OrderErrorCode.ORDER_CREATE_VALID_FAILED;

/**
 * 订单对外提供 Dubbo 接口
 * @Author conglongli
 * @date 2025/3/22 23:54
 */
@DubboService(version = "1.0.0")
public class OrderFacadeServiceImpl implements OrderFacadeService {


    @Autowired
    private OrderManageService orderService;

    @Autowired
    private OrderReadService orderReadService;

    @Autowired
    private InventoryWrapperService inventoryWrapperService;

    @Autowired
    private StreamProducer streamProducer;

    @Autowired
    private UserFacadeService userFacadeService;

    @Autowired
    private CollectionFacadeService collectionFacadeService;

    @Autowired
    private OrderCreateValidator orderValidatorChain;

    @Override
    @DistributeLock(keyExpression = "#request.identifier", scene = "ORDER_CREATE")
    @Facade
    public OrderResponse create(OrderCreateRequest request) {
        try {
            orderValidatorChain.validate(request);
        } catch (OrderException e) {
            return new OrderResponse.OrderResponseBuilder().buildFail(ORDER_CREATE_VALID_FAILED.getCode(), e.getErrorCode().getMessage());
        }

        Boolean preDeductResult = inventoryWrapperService.preDeduct(request);
        if (preDeductResult) {
            return orderService.create(request);
        }
        throw new OrderException(OrderErrorCode.INVENTORY_DEDUCT_FAILED);
    }

    @Override
    @Facade
    public OrderResponse cancel(OrderCancelRequest request) {
        return sendTransactionMsgForClose(request);
    }

    @Override
    @Facade
    public OrderResponse timeout(OrderTimeoutRequest request) {
        return sendTransactionMsgForClose(request);
    }

    @Override
    @Facade
    public OrderResponse confirm(OrderConfirmRequest request) {
        CollectionSaleRequest collectionSaleRequest = new CollectionSaleRequest();
        collectionSaleRequest.setUserId(request.getBuyerId());
        collectionSaleRequest.setCollectionId(request.getCollectionId());
        collectionSaleRequest.setIdentifier(request.getOrderId());
        collectionSaleRequest.setQuantity(request.getItemCount());
        // 库存预扣减
        CollectionSaleResponse response = collectionFacadeService.trySale(collectionSaleRequest);
        // 订单确认
        if (response.getSuccess()) {
            return orderService.confirm(request);
        }

        return new OrderResponse.OrderResponseBuilder().orderId(request.getOrderId()).buildFail(response.getResponseCode(), response.getResponseMessage());
    }

    @Override
    @Facade
    public OrderResponse createAndConfirm(OrderCreateAndConfirmRequest request) {
        try {
            orderValidatorChain.validate(request);
        } catch (OrderException e) {
            return new OrderResponse.OrderResponseBuilder().buildFail(ORDER_CREATE_VALID_FAILED.getCode(), e.getErrorCode().getMessage());
        }

        CollectionSaleRequest collectionSaleRequest = new CollectionSaleRequest(request);
        CollectionSaleResponse response = collectionFacadeService.trySaleWithoutHint(collectionSaleRequest);

        if (!response.getSuccess()) {
            return new OrderResponse.OrderResponseBuilder().buildFail(response.getResponseMessage(), response.getResponseCode());
        }

        return orderService.createAndConfirm(request);
    }


    /**
     * 关单处理：发送关单事务消息
     * @param request
     * @return
     */
    @NotNull
    private OrderResponse sendTransactionMsgForClose(BaseOrderUpdateRequest request) {
        boolean result = streamProducer.send("orderClose-out-0", null, JSON.toJSONString(request), "CLOSE_TYPE", request.getOrderEvent().name());
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setSuccess(result);
        return orderResponse;
    }

    @Override
    @Facade
    public OrderResponse pay(OrderPayRequest request) {
        OrderResponse response = orderService.pay(request);
        if (!response.getSuccess()) {
            TradeOrder existOrder = orderReadService.getOrder(request.getOrderId());
            if (existOrder != null && existOrder.isPaid()) {
                // 对于支付失败的情况，做幂等判断，如果是相同支付流水和支付渠道，则返回成功
                if (existOrder.getPayStreamId().equals(request.getPayStreamId()) && existOrder.getPayChannel() == request.getPayChannel()) {
                    return new OrderResponse.OrderResponseBuilder().orderId(existOrder.getOrderId()).buildSuccess();
                } else {
                    return new OrderResponse.OrderResponseBuilder().orderId(existOrder.getOrderId()).buildFail(OrderErrorCode.ORDER_ALREADY_PAID.getCode(), OrderErrorCode.ORDER_ALREADY_PAID.getMessage());
                }
            }
        }
        return response;
    }

    @Override
    public SingleResponse<TradeOrderVO> getTradeOrder(String orderId) {
        return SingleResponse.of(TradeOrderConvertor.INSTANCE.mapToVo(orderReadService.getOrder(orderId)));
    }

    @Override
    public SingleResponse<TradeOrderVO> getTradeOrder(String orderId, String userId) {
        return SingleResponse.of(TradeOrderConvertor.INSTANCE.mapToVo(orderReadService.getOrder(orderId, userId)));
    }

    @Override
    public PageResponse<TradeOrderVO> pageQuery(OrderPageQueryRequest request) {
        Page<TradeOrder> tradeOrderPage = orderReadService.pageQueryByState(request.getBuyerId(), request.getState(), request.getCurrentPage(), request.getPageSize());
        List<TradeOrderVO> tradeOrderVOS = TradeOrderConvertor.INSTANCE.mapToVo(tradeOrderPage.getRecords());
        tradeOrderVOS.forEach(tradeOrderVO -> tradeOrderVO.setSellerName(getSellerName(tradeOrderVO)));
        return PageResponse.of(tradeOrderVOS, (int)tradeOrderPage.getTotal(), request.getCurrentPage(), request.getPageSize());
    }

    /**
     * 获取卖家名称
     * @param tradeOrderVO
     * @return
     */
    private String getSellerName(TradeOrderVO tradeOrderVO) {
        if(tradeOrderVO.getSellerType() == UserType.PLATFORM) {
            return "平台";
        }
        UserQueryRequest userQueryRequest = new UserQueryRequest(Long.valueOf(tradeOrderVO.getSellerId()));
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(userQueryRequest);
        if(userQueryResponse.getSuccess()) {
            return userQueryResponse.getData().getNickName();
        }
        return "-";
    }


}
