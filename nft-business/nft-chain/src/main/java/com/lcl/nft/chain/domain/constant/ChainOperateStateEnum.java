package com.lcl.nft.chain.domain.constant;

/**
 * @Author conglongli
 * @date 2025/2/18 14:16
 */
public enum ChainOperateStateEnum {
    /**
     * 上链成功
     */
    SUCCEED,
    /**
     * 上链中
     */
    PROCESSING,
    /**
     * 上链失败
     */
    FAILED,
    /**
     * 未处理
     */
    INIT
}
