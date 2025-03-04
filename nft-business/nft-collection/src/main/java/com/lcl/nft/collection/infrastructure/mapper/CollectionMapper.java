package com.lcl.nft.collection.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.collection.domain.entity.Collection;
import org.apache.ibatis.annotations.Mapper;

/**
 * 藏品信息 Mapper 接口
 * @Author conglongli
 * @date 2025/2/26 17:00
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

    /**
     * 根据藏品标识查询藏品信息
     *
     * @param identifier
     * @return
     */
    Collection selectByIdentifier(String identifier);

    /**
     * 库存扣减
     *
     * @param collection
     * @return
     */
    int sale(Collection collection);

    /**
     * 库存确认扣减
     *
     * @param id
     * @param occupiedInventory 变更前的库存
     * @param quantity
     * @return
     */
    int confirmSale(Long id, Long occupiedInventory,Long quantity);

    /**
     * 库存预扣减
     *
     * @param id
     * @param quantity
     * @return
     */
    int trySale(Long id, Long quantity);

    /**
     * 库存预扣减-无hint版
     * @param id
     * @param quantity
     * @return
     */
    int trySaleWithoutHint(Long id, Long quantity);

    /**
     * 库存退回
     *
     * @param id
     * @param quantity
     * @return
     */
    int cancelSale(Long id, Long quantity);
}

