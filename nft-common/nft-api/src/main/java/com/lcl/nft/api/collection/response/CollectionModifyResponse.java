package com.lcl.nft.api.collection.response;

import com.lcl.nft.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class CollectionModifyResponse extends BaseResponse {
    /**
     * 藏品id
     */
    private Long collectionId;
}
