package com.lcl.nft.order.sharding.id;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

/**
 * @Author conglongli
 * @date 2025/1/15 16:45
 */
public class WorkerIdHolder implements CommandLineRunner {

    private RedissonClient redissonClient;

    @Value("${order.client.name:workerId}")
    private String clientName;

    public static long WORKER_ID;

    public WorkerIdHolder(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    @Override
    public void run(String... args) throws Exception {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(clientName);
        WORKER_ID = atomicLong.incrementAndGet() % 32;
    }
}
