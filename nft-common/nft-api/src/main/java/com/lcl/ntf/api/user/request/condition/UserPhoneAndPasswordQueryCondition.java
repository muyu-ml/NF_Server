package com.lcl.ntf.api.user.request.condition;

import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 00:42
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneAndPasswordQueryCondition implements UserQueryCondition {

    private static final long serialVersionUID = 1L;

    /**
     * 用户手机号
     */
    private String telephone;

    /**
     * 密码
     */
    private String password;
}
