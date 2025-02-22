package com.lcl.nft.chain;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lcl.nft.chain")
@EnableDubbo
public class NftChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftChainApplication.class, args);
    }

}
