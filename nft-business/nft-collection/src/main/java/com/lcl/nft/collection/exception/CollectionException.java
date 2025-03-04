package com.lcl.nft.collection.exception;

import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.ErrorCode;

/**
 * 藏品异常
 * @Author conglongli
 * @date 2025/2/27 10:07
 */
public class CollectionException extends BizException {

    public CollectionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CollectionException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CollectionException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public CollectionException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public CollectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

}
