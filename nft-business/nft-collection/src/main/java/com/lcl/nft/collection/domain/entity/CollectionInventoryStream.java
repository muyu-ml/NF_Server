package com.lcl.nft.collection.domain.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.google.common.collect.Maps;
import com.lcl.nft.api.collection.constant.CollectionEvent;
import com.lcl.nft.api.collection.constant.CollectionStateEnum;
import com.lcl.nft.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 藏品库存流水信息
 * @Author conglongli
 * @date 2025/2/25 11:18
 */
@Getter
@Setter
@NoArgsConstructor
public class CollectionInventoryStream extends BaseEntity {

    /**
     * 流水类型
     */
    private CollectionEvent streamType;

    /**
     * '幂等号'
     */
    private String identifier;

    /**
     * '变更数量'
     */
    private Long changedQuantity;

    /**
     * 藏品id
     */
    private Long collectionId;

    /**
     * '价格'
     */
    private BigDecimal price;

    /**
     * '藏品数量'
     */
    private Long quantity;

    /**
     * '可售库存'
     */
    private Long saleableInventory;

    /**
     * '已占库存'
     */
    private Long occupiedInventory;

    /**
     * '状态'
     */
    private CollectionStateEnum state;

    /**
     * 扩展信息
     */
    private String extendInfo;

    public CollectionInventoryStream(Collection collection, String identifier, CollectionEvent streamType, Long quantity) {
        this.collectionId = collection.getId();
        this.price = collection.getPrice();
        this.quantity = collection.getQuantity();
        this.saleableInventory = collection.getSaleableInventory();
        this.occupiedInventory = collection.getOccupiedInventory();
        this.state = collection.getState();
        this.streamType = streamType;
        this.identifier = identifier;
        this.changedQuantity = quantity;
        super.setLockVersion(collection.getLockVersion());
        super.setDeleted(collection.getDeleted());
    }

    public void addHeldCollectionId(Long heldCollectionId) {
        Map<String, Serializable> extendMap;
        if (this.extendInfo == null) {
            extendMap = Maps.newHashMapWithExpectedSize(1);
        } else {

            extendMap = JSON.parseObject(this.extendInfo, HashMap.class);
        }
        extendMap.put("heldCollectionId", heldCollectionId);
        this.extendInfo = JSON.toJSONString(extendMap);
    }

    @JSONField(serialize = false)
    public Long getHeldCollectionId() {
        if (this.extendInfo != null) {
            return ((Integer)JSON.parseObject(this.extendInfo, HashMap.class).get("heldCollectionId")).longValue();
        }

        return null;
    }
}
