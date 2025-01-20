package com.lcl.nft.api.chain.response.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author conglongli
 * @date 2025/1/15 01:54
 */
@Getter
@Setter
@NoArgsConstructor
public class ChainResultData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ntf唯一编号
     */
    private String nftId;

    /**
     * 交易哈希
     */
    private String txHash;

    /**
     * 状态
     */
    private String state;

    /**
     * '藏品编号'
     */
    private String serialNo;


}
