package com.lcl.nft.sms.config;

import com.lcl.nft.sms.MockSmsServiceImpl;
import com.lcl.nft.sms.SmsService;
import com.lcl.nft.sms.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Author conglongli
 * @date 2025/1/14 17:21
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfiguration {
    @Autowired
    private SmsProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @Profile({"default","prod"})
    public SmsService smsService() {
        SmsServiceImpl smsService = new SmsServiceImpl();
        smsService.setHost(properties.getHost());
        smsService.setPath(properties.getPath());
        smsService.setAppcode(properties.getAppcode());
        smsService.setSmsSignId(properties.getSmsSignId());
        smsService.setTemplateId(properties.getTemplateId());
        return smsService;
    }

    @Bean
    @ConditionalOnMissingBean
    @Profile({"dev","test"})
    public SmsService mockSmsService() {
        MockSmsServiceImpl smsService = new MockSmsServiceImpl();
        return smsService;
    }
}
