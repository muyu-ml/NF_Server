package com.lcl.nft.api.pay.response;

import com.lcl.nft.api.pay.model.PayOrderVO;
import com.lcl.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author conglongli
 * @date 2025/3/16 11:28
 */
@Getter
@Setter
public class PayQueryResponse extends BaseResponse {

    private List<PayOrderVO> payOrders;
}
