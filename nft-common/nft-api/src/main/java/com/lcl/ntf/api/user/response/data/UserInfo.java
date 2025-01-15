package com.lcl.ntf.api.user.response.data;

import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyPhone;
import com.lcl.ntf.api.user.constant.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author conglongli
 * @date 2025/1/15 01:26
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    @SensitiveStrategyPhone
    private String telephone;

    /**
     * 状态
     *
     * @see com.lcl.ntf.api.user.constant.UserStateEnum
     */
    private String state;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;

    /**
     * 区块链地址
     */
    private String blockChainUrl;

    /**
     * 区块链平台
     */
    private String blockChainPlatform;

    /**
     * 实名认证
     */
    private Boolean certification;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 注册时间
     */
    private Date createTime;
}