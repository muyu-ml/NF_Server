package com.lcl.nft.api.user.constant;

/**
 * 用户操作类型
 * @Author conglongli
 * @date 2025/1/15 00:38
 */
public enum UserOperateTypeEnum {
    /**
     * 冻结
     */
    FREEZE,

    /**
     * 解冻
     */
    UNFREEZE,

    /**
     * 登录
     */
    LOGIN,
    /**
     * 注册
     */
    REGISTER,
    /**
     * 激活
     */
    ACTIVE,
    /**
     * 实名认证
     */
    AUTH,
    /**
     * 修改信息
     */
    MODIFY
    ;
}
