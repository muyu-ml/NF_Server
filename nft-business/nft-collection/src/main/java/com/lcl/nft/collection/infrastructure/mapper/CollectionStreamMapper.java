package com.lcl.nft.collection.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.collection.domain.entity.CollectionStream;
import org.apache.ibatis.annotations.Mapper;

/**
 * 藏品流水信息 Mapper 接口
 * @Author conglongli
 * @date 2025/2/26 17:12
 */
@Mapper
public interface CollectionStreamMapper extends BaseMapper<CollectionStream> {
    /**
     * 根据标识符查询
     *
     * @param identifier
     * @param streamType
     * @param collectionId
     * @return
     */
    CollectionStream selectByIdentifier(String identifier, String streamType, Long collectionId);

}
