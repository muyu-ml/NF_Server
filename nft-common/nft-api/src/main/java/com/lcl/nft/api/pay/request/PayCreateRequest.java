package com.lcl.nft.api.pay.request;

import com.lcl.nft.base.request.BaseRequest;
import com.lcl.nft.api.common.constant.BizOrderType;
import com.lcl.nft.api.pay.constant.PayChannel;
import com.lcl.nft.api.user.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author conglongli
 * @date 2025/1/15 10:31
 */
@Getter
@Setter
public class PayCreateRequest extends BaseRequest {

    /**
     * 付款方id
     */
    @NotNull(message = "payerId is null")
    private String payerId;

    /**
     * 付款方id类型
     */
    @NotNull(message = "payerType is null")
    private UserType payerType;

    /**
     * 收款方id
     */
    @NotNull(message = "payeeId is null")
    private String payeeId;

    /**
     * 收款方id类型
     */
    @NotNull(message = "payeeType is null")
    private UserType payeeType;

    /**
     * 业务单号
     */
    @NotNull(message = "bizNo is null")
    private String bizNo;

    /**
     * 业务单号类型
     */
    @NotNull(message = "bizType is null")
    private BizOrderType bizType;

    /**
     * 订单金额
     */
    @NotNull(message = "orderAmount is null")
    private BigDecimal orderAmount;

    /**
     * 支付渠道
     */
    @NotNull(message = "payChannel is null")
    private PayChannel payChannel;

    /**
     * 支付备注
     */
    @NotNull(message = "memo is null")
    private String memo;

}
