package com.lcl.ntf.api.order.request;

import com.lcl.nft.base.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class OrderPageQueryRequest extends PageRequest {

    /**
     * 买家id
     */
    private String buyerId;

    /**
     * 卖家id
     */
    private String sellerId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单状态
     */
    private String state;
}
