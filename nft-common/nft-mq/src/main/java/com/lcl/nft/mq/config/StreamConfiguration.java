package com.lcl.nft.mq.config;

import com.lcl.nft.mq.producer.StreamProducer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author conglongli
 * @date 2025/1/14 19:32
 */
@Configuration
public class StreamConfiguration {

    @Bean
    public StreamProducer streamBridge(){
        StreamProducer streamProducer = new StreamProducer();
        return streamProducer;
    }
}
