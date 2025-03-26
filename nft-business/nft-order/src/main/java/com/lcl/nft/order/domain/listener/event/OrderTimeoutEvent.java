package com.lcl.nft.order.domain.listener.event;

import com.lcl.nft.api.order.request.BaseOrderRequest;
import org.springframework.context.ApplicationEvent;

/**
 * @Author conglongli
 * @date 2025/3/15 11:05
 */
public class OrderTimeoutEvent extends ApplicationEvent {
    public OrderTimeoutEvent(BaseOrderRequest baseOrderRequest) {
        super(baseOrderRequest);
    }
}
