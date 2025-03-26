package com.lcl.nft.order.domain.listener;

import com.lcl.nft.api.order.OrderFacadeService;
import com.lcl.nft.api.order.request.OrderConfirmRequest;
import com.lcl.nft.api.user.constant.UserType;
import com.lcl.nft.order.domain.entity.TradeOrder;
import com.lcl.nft.order.domain.listener.event.OrderCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author conglongli
 * @date 2025/3/15 11:04
 */
@Component
public class OrderEventListener {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @EventListener(OrderCreateEvent.class)
    @Async("orderListenExecutor")
    public void onApplicationEvent(OrderCreateEvent event) {

        TradeOrder tradeOrder = (TradeOrder) event.getSource();
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

