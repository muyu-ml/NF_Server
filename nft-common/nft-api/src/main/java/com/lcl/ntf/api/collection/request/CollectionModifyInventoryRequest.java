package com.lcl.ntf.api.collection.request;

import com.lcl.ntf.api.collection.constant.CollectionEvent;
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
public class CollectionModifyInventoryRequest extends BaseCollectionRequest {

    /**
     * '藏品数量'
     */
    private Long quantity;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.MODIFY_INVENTORY;
    }
}
