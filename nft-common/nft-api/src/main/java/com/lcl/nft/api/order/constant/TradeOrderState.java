package com.lcl.nft.api.order.constant;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public enum TradeOrderState {

    /**
     * 订单创建
     */
    CREATE,

    /**
     * 订单确认
     */
    CONFIRM,
    /**
     * 已付款
     */
    PAID,
    /**
     * 交易成功
     */
    FINISH,
    /**
     * 订单关闭
     */
    CLOSED;
}
