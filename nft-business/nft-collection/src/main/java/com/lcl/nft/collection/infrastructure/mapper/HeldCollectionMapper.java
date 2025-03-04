package com.lcl.nft.collection.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.collection.domain.entity.HeldCollection;
import org.apache.ibatis.annotations.Mapper;

/**
 * 藏品持有信息 Mapper 接口
 * @Author conglongli
 * @date 2025/2/26 17:12
 */
@Mapper
public interface HeldCollectionMapper extends BaseMapper<HeldCollection> {

}
