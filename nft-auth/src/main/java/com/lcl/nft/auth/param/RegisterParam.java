package com.lcl.nft.auth.param;

import com.lcl.nft.base.validator.IsMobile;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/16 11:23
 */
@Setter
@Getter
public class RegisterParam {

    /**
     * 手机号
     */
    @IsMobile
    private String telephone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 邀请码
     */
    private String inviteCode;
}
