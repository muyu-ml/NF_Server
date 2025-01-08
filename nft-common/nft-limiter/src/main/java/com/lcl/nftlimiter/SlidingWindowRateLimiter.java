package com.lcl.nftlimiter;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

/**
 * 滑动窗口限流
 * @Author conglongli
 * @date 2025/1/8 17:16
 */
public class SlidingWindowRateLimiter implements RateLimiter{

    private RedissonClient redissonClient;
    private static final String LIMIT_KEY_PREFIX = "nft:turbo:limit:";

    public SlidingWindowRateLimiter(RedissonClient redisson) {
        this.redissonClient = redisson;
    }

    @Override
    public Boolean tryAcquire(String key, int limit, int windowSize) {
        RRateLimiter rRateLimiter = redissonClient.getRateLimiter(LIMIT_KEY_PREFIX + key);
        if(!rRateLimiter.isExists()) {
            rRateLimiter.trySetRate(RateType.OVERALL, limit, windowSize, RateIntervalUnit.SECONDS);
        }
        return rRateLimiter.tryAcquire();
    }
}
