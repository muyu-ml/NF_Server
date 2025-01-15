package com.lcl.ntf.api.user.constant;

/**
 * @Author conglongli
 * @date 2025/1/15 00:39
 */
public enum UserType {
    /**
     * 用户
     */
    CUSTOMER("用户"),

    /**
     * 平台
     */
    PLATFORM("平台");

    private String desc;

    UserType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
