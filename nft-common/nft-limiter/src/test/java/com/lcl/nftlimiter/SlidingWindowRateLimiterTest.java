package com.lcl.nftlimiter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author conglongli
 * @date 2025/1/8 22:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RateLimiterTestConfiguration.class})
@ActiveProfiles("test")
public class SlidingWindowRateLimiterTest {
    @Autowired
    SlidingWindowRateLimiter slidingWindowRateLimiter;

    @Test
    @Ignore
    public void tryAcquire() {
        String key = "testLimiter111";
        Boolean result = slidingWindowRateLimiter.tryAcquire(key, 3, 10);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire(key, 3, 10);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire(key, 3, 10);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire(key, 3, 10);
        Assert.assertFalse(result);
        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }
        result = slidingWindowRateLimiter.tryAcquire(key, 3, 10);
        Assert.assertTrue(result);
    }


    @Test
    @Ignore
    public void tryAcquire1() {
        String key = "testLimiter222";
        Boolean result = slidingWindowRateLimiter.tryAcquire(key, 1, 5);
        Assert.assertTrue(result);
        result = slidingWindowRateLimiter.tryAcquire(key, 1, 5);
        Assert.assertFalse(result);
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Boolean result2 = slidingWindowRateLimiter.tryAcquire(key, 1, 1);
        Assert.assertFalse(result2);

        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Boolean result3 = slidingWindowRateLimiter.tryAcquire(key, 1, 1);
        Assert.assertTrue(result3);
    }
}
