package com.lcl.ntf.api.pay.constant;

/**
 * @Author conglongli
 * @date 2025/1/15 10:29
 */
public enum PayRefundOrderState {

    /**
     * 待退款
     */
    TO_REFUND,

    /**
     * 退款中
     */
    REFUNDING,

    /**
     * 已退款
     */
    REFUNDED;
}
