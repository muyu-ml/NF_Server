package com.lcl.nft.api.pay.model;

import com.lcl.nft.api.pay.constant.PayOrderState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author conglongli
 * @date 2025/1/15 10:30
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PayOrderVO implements Serializable {

    private String payOrderId;

    private String payUrl;

    private PayOrderState orderState;

    private static final long serialVersionUID = 1L;
}

