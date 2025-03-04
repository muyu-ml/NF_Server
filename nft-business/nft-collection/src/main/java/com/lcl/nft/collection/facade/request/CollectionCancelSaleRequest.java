package com.lcl.nft.collection.facade.request;

import com.lcl.nft.api.collection.constant.CollectionEvent;

/**
 * @Author conglongli
 * @date 2025/2/26 17:20
 */
public record CollectionCancelSaleRequest(String identifier, Long collectionId,Long quantity) {

    public CollectionEvent eventType() {
        return CollectionEvent.CANCEL_SALE;
    }
}
