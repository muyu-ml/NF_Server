package com.lcl.nft.api.collection.request;

import com.lcl.nft.api.collection.constant.CollectionEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CollectionRemoveRequest extends BaseCollectionRequest {

    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.REMOVE;
    }
}
