package com.lcl.nft.chain;

import com.alibaba.fastjson2.JSON;
import com.lcl.nft.mq.producer.StreamProducer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class MqTest extends ChainBaseTest {
    @Autowired
    private StreamProducer streamProducer;

    @Test
    public void test() {
        Map test=new HashMap();
        test.put("key","test");
        test.put("value","123");
        streamProducer.send("chain-out-0","test",JSON.toJSONString(test));
    }
}
