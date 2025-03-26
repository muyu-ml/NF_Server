package com.lcl.nft.order.infrastructure.config;

import com.lcl.nft.api.collection.service.CollectionFacadeService;
import com.lcl.nft.api.goods.service.GoodsFacadeService;
import com.lcl.nft.api.pay.service.PayFacadeService;
import com.lcl.nft.api.user.service.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 引用外部 Dubbo 接口，作为 Bean 使用
 * @Author conglongli
 * @date 2025/3/16 11:01
 */
@Configuration
public class OrderDubboConfiguration {
    @DubboReference(version = "1.0.0")
    private CollectionFacadeService collectionFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private PayFacadeService payFacadeService;

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "collectionFacadeService")
    public CollectionFacadeService collectionFacadeService() {
        return this.collectionFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "userFacadeService")
    public UserFacadeService userFacadeService() {
        return this.userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "payFacadeService")
    public PayFacadeService payFacadeService() {
        return this.payFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "goodsFacadeService")
    public GoodsFacadeService goodsFacadeService() {
        return this.goodsFacadeService;
    }
}
