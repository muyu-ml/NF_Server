package com.lcl.nft.order.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lcl.nft.order.domain.entity.TradeOrderStream;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单流水Mapper
 * @Author conglongli
 * @date 2025/3/16 11:30
 */
@Mapper
public interface OrderStreamMapper extends BaseMapper<TradeOrderStream> {

    /**
     * 根据流标识查询
     *
     * @param streamIdentifier
     * @param streamType
     * @param orderId
     * @return
     */
    TradeOrderStream selectByIdentifier(String streamIdentifier, String streamType, String orderId);
}

