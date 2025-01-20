package com.lcl.nft.api.pay.constant;

/**
 * 支付渠道
 * @Author conglongli
 * @date 2025/1/15 10:27
 */
public enum PayChannel {

    /**
     * 支付宝
     */
    ALIPAY("支付宝"),
    /**
     * 微信
     */
    WECHAT("微信"),

    /**
     * MOCK
     */
    MOCK("MOCK");

    private String value;

    PayChannel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
