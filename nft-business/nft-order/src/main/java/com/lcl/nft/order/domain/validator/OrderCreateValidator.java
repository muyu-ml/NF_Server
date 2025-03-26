package com.lcl.nft.order.domain.validator;

import com.lcl.nft.api.order.request.OrderCreateRequest;
import com.lcl.nft.order.domain.exception.OrderException;

/**
 * 订单校验
 * @Author conglongli
 * @date 2025/3/15 11:14
 */
public interface OrderCreateValidator {
    /**
     * 设置下一个校验器
     *
     * @param nextValidator
     */
    public void setNext(OrderCreateValidator nextValidator);

    /**
     * 返回下一个校验器
     *
     * @return
     */
    public OrderCreateValidator getNext();

    /**
     * 校验
     *
     * @param request
     * @throws OrderException 订单异常
     */
    public void validate(OrderCreateRequest request) throws OrderException;
}
