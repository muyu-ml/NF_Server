package com.lcl.nft.collection.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.entity.CollectionSnapshot;
import org.apache.ibatis.annotations.Mapper;

/**
 * 藏品快照信息 Mapper 接口
 * @Author conglongli
 * @date 2025/2/26 17:11
 */
@Mapper
public interface CollectionSnapshotMapper extends BaseMapper<CollectionSnapshot> {

    /**
     * 根据藏品标识查询藏品信息
     * @param collectionId
     * @param version
     * @return
     */
    Collection selectByVersion(String collectionId, Integer version);
}
