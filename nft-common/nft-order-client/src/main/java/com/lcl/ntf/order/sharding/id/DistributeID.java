package com.lcl.nft.order.sharding.id;

import cn.hutool.core.util.IdUtil;
import com.lcl.nft.api.common.constant.BusinessCode;
import com.lcl.nft.order.sharding.strategy.DefaultShardingTableStrategy;
import org.apache.commons.lang3.StringUtils;

/**
 * 分布式ID
 * @Author conglongli
 * @date 2025/1/15 14:58
 */
public class DistributeID {

    /**
     * 系统标识码
     */
    private String businessCode;

    /**
     * 表下标
     */
    private String table;

    /**
     * 序列号
     */
    private String seq;

    /**
     * 分表策略
     */
    private static DefaultShardingTableStrategy shardingTableStrategy = new DefaultShardingTableStrategy();

    public DistributeID() {
    }

    /**
     * 利用雪花算法生成一个唯一ID
     * @param businessCode 业务场景
     * @param workerId 应用节点
     * @param externalId 外部id（例如人员工号）
     * @return
     */
    public static String generateWithSnowflake(BusinessCode businessCode, long workerId,
                                               String externalId){
        long id = IdUtil.getSnowflake(workerId).nextId();
        return generate(businessCode, externalId, id);
    }

    /**
     * 生成一个唯一ID：10（业务码） 1769649671860822016（sequence) 1023(分表）
     * @param businessCode 业务场景
     * @param externalId 外部id（例如人员工号）
     * @param sequenceNumber 雪花算法生成的id
     * @return
     */
    public static String generate(BusinessCode businessCode,
                                  String externalId, Long sequenceNumber) {
        DistributeID distributeID = create(businessCode, externalId, sequenceNumber);
        return distributeID.businessCode + distributeID.seq + distributeID.table;
    }

    @Override
    public String toString() {
        return this.businessCode + this.seq + this.table;
    }

    public static DistributeID create(BusinessCode businessCode,
                                      String externalId, Long sequenceNumber) {
        DistributeID distributeID = new DistributeID();
        distributeID.businessCode = businessCode.getCodeString();
        String table = String.valueOf(shardingTableStrategy.getTable(externalId, businessCode.tableCount()));
        distributeID.table = StringUtils.leftPad(table, 4, "0");
        distributeID.seq = String.valueOf(sequenceNumber);
        return distributeID;
    }

    public static String getShardingTable(DistributeID distributeId){
        return distributeId.table;
    }

    public static String getShardingTable(String externalId, int tableCount) {
        return StringUtils.leftPad(String.valueOf(shardingTableStrategy.getTable(externalId, tableCount)), 4, "0");
    }

    public static String getShardingTable(String id){
        return getShardingTable(valueOf(id));
    }

    public static DistributeID valueOf(String id) {
        DistributeID distributeId = new DistributeID();
        distributeId.businessCode = id.substring(0, 2);
        distributeId.seq = id.substring(2, id.length() - 4);
        distributeId.table = id.substring(id.length() - 4, id.length());
        return distributeId;
    }

}
