package com.lcl.nft.auth.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/16 11:22
 */
@Setter
@Getter
public class LoginParam extends RegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
