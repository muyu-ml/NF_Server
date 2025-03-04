package com.lcl.nft.collection.facade.request;

import com.lcl.nft.api.collection.constant.CollectionEvent;

/**
 * @Author conglongli
 * @date 2025/2/26 17:21
 */
public record CollectionTrySaleRequest(String identifier, Long collectionId, Long quantity) {

    public CollectionEvent eventType() {
        return CollectionEvent.TRY_SALE;
    }
}