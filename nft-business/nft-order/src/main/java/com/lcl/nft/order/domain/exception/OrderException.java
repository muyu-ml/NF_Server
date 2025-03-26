package com.lcl.nft.order.domain.exception;

import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.ErrorCode;

/**
 * @Author conglongli
 * @date 2025/3/15 11:03
 */
public class OrderException extends BizException {
    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public OrderException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public OrderException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public OrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }
}

