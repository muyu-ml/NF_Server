package com.lcl.nft.base.nftbase.statemachine;

/**
 * @Author conglongli
 * @date 2025/1/6 01:12
 */
public interface StateMachine<STATE, EVENT> {

    /**
     * 状态机转移
     *
     * @param state
     * @param event
     * @return
     */
    public STATE transition(STATE state, EVENT event);
}
