package com.lcl.nft.api.collection.response;

import com.lcl.nft.api.collection.constant.CollectionInventoryModifyType;
import lombok.Getter;
import lombok.Setter;

/**
 * 藏品库存修改响应
 *
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class CollectionInventoryModifyResponse extends CollectionModifyResponse {
    /**
     * 修改类型
     */
    private CollectionInventoryModifyType modifyType;

    /**
     * 修改的数量
     */
    private Long quantityModified;
}
