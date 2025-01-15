package com.lcl.ntf.api.pay.constant;

/**
 * @Author conglongli
 * @date 2025/1/15 10:29
 */
public enum PayOrderState {

    /**
     * 待支付
     */
    TO_PAY,

    /**
     * 支付中
     */
    PAYING,

    /**
     * 已付款
     */
    PAID,

    /**
     * 支付超时
     */
    EXPIRED,

    /**
     * 已退款
     */
    REFUNDED;
}
