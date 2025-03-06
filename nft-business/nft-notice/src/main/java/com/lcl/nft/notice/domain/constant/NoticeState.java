package com.lcl.nft.notice.domain.constant;

/**
 * @Author conglongli
 * @date 2025/3/5 14:08
 */
public enum NoticeState {
    /**
     * 初始化
     */
    INIT,

    /**
     * 已发送成功
     */
    SUCCESS,

    /**
     * 发送失败
     */
    FAILED,

    /**
     * 已挂起
     */
    SUSPENDED;
}
