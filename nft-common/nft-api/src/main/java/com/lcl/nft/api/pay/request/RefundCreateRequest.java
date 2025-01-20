package com.lcl.nft.api.pay.request;

import com.lcl.nft.base.request.BaseRequest;
import com.lcl.nft.api.pay.constant.PayChannel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class RefundCreateRequest extends BaseRequest {

    /**
     * 支付单号
     */
    private String payOrderId;

    /**
     * 需要退款的金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款幂等号
     */
    private String identifier;

    /**
     * 退款渠道
     */
    private PayChannel refundChannel;

    /**
     * 退款备注
     */
    private String memo;
}
