package com.lcl.nft.chain.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.chain.domain.entity.ChainOperateInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 链操作 Mapper 接口
 * @Author conglongli
 * @date 2025/2/18 14:13
 */

@Mapper
public interface ChainOperateInfoMapper extends BaseMapper<ChainOperateInfo> {
    /**
     * 扫描所有
     *
     * @return
     */
    List<ChainOperateInfo> scanAll();

    /**
     * 根据 ID 查询出最小的 ID
     * @param state
     * @return
     */
    public Long queryMinIdByState(String state);
}
