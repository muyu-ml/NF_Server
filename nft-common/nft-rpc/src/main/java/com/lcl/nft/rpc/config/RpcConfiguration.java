package com.lcl.nft.rpc.config;

import com.lcl.nft.rpc.facade.FacadeAspect;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rpc 配置
 * @Author conglongli
 * @date 2025/1/13 11:08
 */
@EnableDubbo
@Configuration
public class RpcConfiguration {
    @Bean
    public FacadeAspect facadeAspect(){
        return new FacadeAspect();
    }
}
