package com.lcl.nft.sms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author conglongli
 * @date 2025/1/14 17:20
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SmsProperties.PREFIX)
public class SmsProperties {
    public static final String PREFIX = "spring.sms";

    private String host;

    private String path;

    private String appcode;

    private String smsSignId;

    private String templateId;
}
