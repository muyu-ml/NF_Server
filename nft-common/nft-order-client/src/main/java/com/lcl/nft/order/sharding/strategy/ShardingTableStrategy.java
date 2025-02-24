package com.lcl.nft.order.sharding.strategy;

/**
 * @Author conglongli
 * @date 2025/1/15 14:59
 */
public interface ShardingTableStrategy {

    /**
     * 获取分表结果
     *
     * @param externalId 外部id
     * @param tableCount 表数量
     * @return 分表结果
     */
    public int getTable(String externalId, int tableCount);
}
