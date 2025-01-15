package com.lcl.ntf.order.config;

import com.lcl.ntf.order.sharding.id.WorkerIdHolder;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author conglongli
 * @date 2025/1/15 16:48
 */
@Configuration
public class OrderClientConfiguration {

    @Bean
    public WorkerIdHolder workerIdHolder(RedissonClient redisson) {
        return new WorkerIdHolder(redisson);
    }
}
