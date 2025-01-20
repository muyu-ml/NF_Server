package com.lcl.nft.user.domain.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置
 * @Author conglongli
 * @date 2025/1/19 00:04
 */
@ConfigurationProperties(prefix = AuthProperties.PREFIX)
@Getter
@Setter
public class AuthProperties {
    public static final String PREFIX = "spring.auth";

    private String host;

    private String path;

    private String appcode;

}
