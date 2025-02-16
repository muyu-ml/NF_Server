package com.lcl.nft.job.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author conglongli
 * @date 2025/1/13 00:59
 */
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
@Getter
@Setter
public class XxlJobProperties {

    public static final String PREFIX = "spring.xxl.job";

    private boolean enabled;

    private String adminAddresses;

    private String accessToken;

    private String appName;

    private String ip;

    private int port;

    private String logPath;

    private int logRetentionDays = 30;
}
