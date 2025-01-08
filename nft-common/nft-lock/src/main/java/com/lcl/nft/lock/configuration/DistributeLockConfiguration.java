package com.lcl.nft.lock.configuration;

import com.lcl.nft.lock.DistributeLockAspect;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author conglongli
 * @date 2025/1/9 01:12
 */
@Configuration
public class DistributeLockConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DistributeLockAspect distributeLockAspect(RedissonClient redissonClient) {
        return new DistributeLockAspect(redissonClient);
    }
}
