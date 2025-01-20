package com.lcl.nft.api.order.request;

import com.lcl.nft.api.order.constant.TradeOrderEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class OrderConfirmRequest extends BaseOrderUpdateRequest {

    /**
     * 买家Id
     */
    private String buyerId;

    /**
     * 藏品Id
     */
    private Long collectionId;

    /**
     * 数量
     */
    private Long itemCount;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.CONFIRM;
    }
}

