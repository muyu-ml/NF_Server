package com.lcl.nft.api.user.constant;

/**
 * 用户状态
 * @Author conglongli
 * @date 2025/1/15 00:39
 */
public enum UserStateEnum {
    /**
     * 创建成功
     */
    INIT,
    /**
     * 实名认证
     */
    AUTH,
    /**
     * 上链成功
     */
    ACTIVE,

    /**
     * 冻结
     */
    FROZEN;
}
