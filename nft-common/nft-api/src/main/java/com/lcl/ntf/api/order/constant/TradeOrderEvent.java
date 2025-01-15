package com.lcl.ntf.api.order.constant;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public enum TradeOrderEvent {

    /**
     * 订单创建
     */
    CREATE,

    /**
     * 订单确认
     */
    CONFIRM,

    /**
     * 订单创建并确认
     */
    CREATE_AND_CONFIRM,

    /**
     * 订单支付
     */
    PAY,

    /**
     * 订单取消
     */
    CANCEL,

    /**
     * 订单超时
     */
    TIME_OUT,

    /**
     * 订单完成
     */
    FINISH;
}
