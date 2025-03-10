package com.lcl.nft.order;

import cn.hutool.core.util.IdUtil;
import com.lcl.nft.api.common.constant.BusinessCode;
import com.lcl.nft.order.sharding.id.DistributeID;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class DistributeIDTest {

    @Test
    public void generate() {
        System.out.println(IdUtil.getSnowflake(BusinessCode.TRADE_ORDER.code()).nextId());
        Assert.assertEquals(DistributeID.generate(BusinessCode.TRADE_ORDER, "6", 1769649671860822016L), "1017696496718608220160002");
    }

    @Test
    public void testValueOf() {
        DistributeID distributeID = DistributeID.valueOf("109525038028500006");
        Assert.assertEquals("109525038028500006", distributeID.toString());
    }

    @Test
    public void testGetShardingTable() {
        Assert.assertEquals(DistributeID.getShardingTable("109525038028500006"), "0006");
    }

}