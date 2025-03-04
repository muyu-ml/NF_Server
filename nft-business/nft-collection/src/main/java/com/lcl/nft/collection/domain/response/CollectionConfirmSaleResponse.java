package com.lcl.nft.collection.domain.response;

import com.lcl.nft.base.response.BaseResponse;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.entity.HeldCollection;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/2/25 11:30
 */
@Setter
@Getter
public class CollectionConfirmSaleResponse extends BaseResponse {

    private Collection collection;

    private HeldCollection heldCollection;
}
