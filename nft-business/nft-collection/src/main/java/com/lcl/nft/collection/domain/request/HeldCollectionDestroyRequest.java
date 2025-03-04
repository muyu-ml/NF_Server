package com.lcl.nft.collection.domain.request;

import com.lcl.nft.collection.domain.constant.HeldCollectionEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author conglongli
 * @date 2025/2/25 11:28
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class HeldCollectionDestroyRequest extends BaseHeldCollectionRequest {

    @Override
    public HeldCollectionEventType getEventType() {
        return HeldCollectionEventType.DESTORY;
    }
}
