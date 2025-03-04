package com.lcl.nft.collection.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.collection.domain.entity.CollectionInventoryStream;
import org.apache.ibatis.annotations.Mapper;

/**
 * 藏品库存流水信息 Mapper 接口
 * @Author conglongli
 * @date 2025/2/26 09:59
 */
@Mapper
public interface CollectionInventoryStreamMapper extends BaseMapper<CollectionInventoryStream> {
    /**
     * 根据标识符查询
     *
     * @param identifier
     * @param streamType
     * @param collectionId
     * @return
     */
    CollectionInventoryStream selectByIdentifier(String identifier, String streamType, Long collectionId);

}
