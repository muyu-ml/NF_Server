package com.lcl.nft.api.pay.service;

import com.lcl.nft.api.pay.model.PayOrderVO;
import com.lcl.nft.api.pay.request.PayCreateRequest;
import com.lcl.nft.api.pay.request.PayQueryRequest;
import com.lcl.nft.api.pay.response.PayCreateResponse;
import com.lcl.nft.base.response.MultiResponse;
import com.lcl.nft.base.response.SingleResponse;

/**
 * @Author conglongli
 * @date 2025/3/16 11:29
 */
public interface PayFacadeService {

    /**
     * 生成支付链接
     *
     * @param payCreateRequest
     * @return
     */
    public PayCreateResponse generatePayUrl(PayCreateRequest payCreateRequest);

    /**
     * 查询支付订单
     *
     * @param payQueryRequest
     * @return
     */
    public MultiResponse<PayOrderVO> queryPayOrders(PayQueryRequest payQueryRequest);

    /**
     * 查询支付订单
     *
     * @param payOrderId
     * @return
     */
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId);

    /**
     * 查询支付订单
     *
     * @param payOrderId
     * @param payerId
     * @return
     */
    public SingleResponse<PayOrderVO> queryPayOrder(String payOrderId, String payerId);


}
