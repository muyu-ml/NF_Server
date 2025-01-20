package com.lcl.nft.api.collection.request;

import com.lcl.nft.api.collection.constant.CollectionEvent;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionModifyPriceRequest extends BaseCollectionRequest {

    /**
     * '价格'
     */
    private BigDecimal price;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.MODIFY_PRICE;
    }
}
