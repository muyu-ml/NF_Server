package com.lcl.nft.base.nftbase.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 通用出参
 * @Author conglongli
 * @date 2025/1/6 00:56
 */
@Setter
@Getter
@ToString
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String responseCode;

    private String responseMessage;
}
