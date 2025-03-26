package com.lcl.nft.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lcl.nft.order")
@EnableDubbo
public class NftOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftOrderApplication.class, args);
    }

}
