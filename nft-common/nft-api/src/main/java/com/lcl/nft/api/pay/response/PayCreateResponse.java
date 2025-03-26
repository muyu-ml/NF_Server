package com.lcl.nft.api.pay.response;

import com.lcl.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/3/16 11:28
 */
@Getter
@Setter
public class PayCreateResponse extends BaseResponse {

    private String payOrderId;

    private String payUrl;
}

