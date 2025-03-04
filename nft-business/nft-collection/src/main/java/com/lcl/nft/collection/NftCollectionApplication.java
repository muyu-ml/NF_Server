package com.lcl.nft.collection;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lcl.nft.collection")
@EnableDubbo
public class NftCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftCollectionApplication.class, args);
    }

}
