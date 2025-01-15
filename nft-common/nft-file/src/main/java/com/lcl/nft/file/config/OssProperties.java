package com.lcl.nft.file.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author conglongli
 * @date 2025/1/13 01:25
 */
@ConfigurationProperties(prefix = OssProperties.PREFIX)
@Getter
@Setter
public class OssProperties {
    public static final String PREFIX = "spring.oss";

    private String bucket;

    private String endPoint;

    private String accessKey;

    private String accessSecret;
}
