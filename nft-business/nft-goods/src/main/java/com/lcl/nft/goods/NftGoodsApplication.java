package com.lcl.nft.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lcl.nft.goods")
@EnableDubbo
public class NftGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NftGoodsApplication.class, args);
    }

}
