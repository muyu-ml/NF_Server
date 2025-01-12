package com.lcl.nft.file.config;

import com.lcl.nft.file.FileService;
import com.lcl.nft.file.MockFileServiceImpl;
import com.lcl.nft.file.OssServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Author conglongli
 * @date 2025/1/13 01:26
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {
    @Autowired
    private OssProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @Profile({"default", "prod"})
    public FileService ossService() {
        OssServiceImpl ossService = new OssServiceImpl();
        ossService.setBucket(properties.getBucket());
        ossService.setEndPoint(properties.getEndPoint());
        ossService.setAccessKey(properties.getAccessKey());
        ossService.setAccessSecret(properties.getAccessSecret());
        return ossService;
    }

    @Bean
    @ConditionalOnMissingBean
    @Profile({"dev", "test"})
    public FileService mockFileService() {
        return new MockFileServiceImpl();
    }
}
