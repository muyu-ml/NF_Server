package com.lcl.nftlimiter;

/**
 * 限流服务
 * @Author conglongli
 * @date 2025/1/8 17:14
 */
public interface RateLimiter {

    /**
     * 判断一个key是否可以通过
     * @param key 限流的key
     * @param limit 限流的数量
     * @param windowSize 窗口大小，单位为秒
     * @return
     */
    Boolean tryAcquire(String key, int limit, int windowSize);
}
