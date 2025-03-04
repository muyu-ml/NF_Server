package com.lcl.nft.collection.domain.response;

import com.lcl.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/2/26 09:37
 */
@Getter
@Setter
public class CollectionInventoryResponse extends BaseResponse {

    private String collectionId;

    private String identifier;

    private Integer inventory;
}
