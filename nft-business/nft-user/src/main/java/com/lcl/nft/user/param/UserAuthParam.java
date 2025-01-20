package com.lcl.nft.user.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户认证参数
 * @Author conglongli
 * @date 2025/1/18 23:06
 */
@Setter
@Getter
public class UserAuthParam {

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;

}

