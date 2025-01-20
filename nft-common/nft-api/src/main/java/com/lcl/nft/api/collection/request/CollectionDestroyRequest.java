package com.lcl.nft.api.collection.request;

import com.lcl.nft.api.collection.constant.CollectionEvent;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDestroyRequest extends BaseCollectionRequest {
    /**
     * '持有藏品id'
     */
    private Long heldCollectionId;

    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.DESTROY;
    }
}
