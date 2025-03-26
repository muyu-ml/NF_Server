package com.lcl.nft.order.domain.listener.event;

import com.lcl.nft.order.domain.entity.TradeOrder;
import org.springframework.context.ApplicationEvent;

/**
 * @Author conglongli
 * @date 2025/3/15 11:05
 */
public class OrderCreateEvent extends ApplicationEvent {

    public OrderCreateEvent(TradeOrder tradeOrder) {
        super(tradeOrder);
    }
}

