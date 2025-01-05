package com.lcl.nft.base.nftbase.exception;

/**
 * 错误码
 * @Author conglongli
 * @date 2025/1/5 03:45
 */
public interface ErrorCode {

    /**
     * 获取错误码
     * @return
     */
    String getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getMessage();
}
