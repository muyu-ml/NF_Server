package com.lcl.nft.notice.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.notice.domain.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author conglongli
 * @date 2025/3/5 14:37
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
