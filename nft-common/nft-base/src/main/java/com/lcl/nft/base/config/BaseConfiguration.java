package com.lcl.nft.base.config;

import com.lcl.nft.base.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用模块的配置类
 * @Author conglongli
 * @date 2025/1/5 03:40
 */
@Configuration
public class BaseConfiguration {
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
