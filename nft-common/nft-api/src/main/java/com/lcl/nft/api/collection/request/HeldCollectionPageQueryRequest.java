package com.lcl.nft.api.collection.request;

import com.lcl.nft.base.request.PageRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class HeldCollectionPageQueryRequest extends PageRequest {

    private String state;

    private String userId;

    private String keyword;
}
