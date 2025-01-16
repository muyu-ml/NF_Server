package com.lcl.nft.auth.exception;

import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.ErrorCode;

/**
 * 认证异常
 * @Author conglongli
 * @date 2025/1/16 11:27
 */
public class AuthException extends BizException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AuthException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause, errorCode);
    }

    public AuthException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }

    public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace, errorCode);
    }

}
