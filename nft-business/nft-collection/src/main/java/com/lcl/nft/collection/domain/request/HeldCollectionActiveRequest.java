package com.lcl.nft.collection.domain.request;

import com.lcl.nft.collection.domain.constant.HeldCollectionEventType;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/2/25 11:27
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HeldCollectionActiveRequest extends BaseHeldCollectionRequest {

    /**
     * 'nftId'
     */
    private String nftId;

    /**
     * 'txHash'
     */
    private String txHash;

    @Override
    public HeldCollectionEventType getEventType() {
        return HeldCollectionEventType.ACTIVE;
    }
}

