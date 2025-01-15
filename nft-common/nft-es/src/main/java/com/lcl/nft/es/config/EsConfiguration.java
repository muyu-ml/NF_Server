package com.lcl.nft.es.config;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @Author conglongli
 * @date 2025/1/13 00:50
 */
@Configuration
@EsMapperScan("com.lcl.nft.*.infrastructure.es.mapper")
@ConditionalOnProperty(value = "easy-es.enable", havingValue = "true")
public class EsConfiguration {
}
