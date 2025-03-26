package com.lcl.nft.order.domain.listener.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @Author conglongli
 * @date 2025/3/15 11:07
 */

@Configuration
@EnableAsync
public class OrderListenerConfig {

    @Bean("orderListenExecutor")
    public Executor orderListenExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("orderListener-%d").build();
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        return executorService;
    }
}
