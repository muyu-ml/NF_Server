package com.lcl.nft.collection.domain.request;

import com.lcl.nft.base.request.BaseRequest;
import com.lcl.nft.collection.domain.constant.HeldCollectionEventType;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/2/25 11:26
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseHeldCollectionRequest extends BaseRequest {
    /**
     * 幂等号
     */
    private String identifier;

    /**
     * '持有藏品id'
     */
    private Long heldCollectionId;

    /**
     * 事件类型
     *
     * @return
     */
    public abstract HeldCollectionEventType getEventType();
}

