package com.lcl.nft.collection.domain.request;

import com.lcl.nft.collection.domain.constant.HeldCollectionEventType;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/2/25 11:28
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HeldCollectionTransferRequest extends BaseHeldCollectionRequest {

    /**
     * '买家id'
     */
    private Long buyerId;

    /**
     * '卖家id'
     */
    private Long sellerId;

    @Override
    public HeldCollectionEventType getEventType() {
        return HeldCollectionEventType.TRANSFER;
    }
}
