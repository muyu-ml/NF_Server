package com.lcl.nft.cache.config;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @Author conglongli
 * @date 2025/1/8 14:53
 */
@Configuration
@EnableMethodCache(basePackages = "com.lcl.nft")
public class CacheConfiguration {
}
