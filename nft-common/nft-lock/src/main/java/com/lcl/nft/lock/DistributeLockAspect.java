package com.lcl.nft.lock;

import cn.hutool.log.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面
 * @Author conglongli
 * @date 2025/1/8 23:12
 */
@Aspect
@Component
public class DistributeLockAspect {

    private RedissonClient redissonClient;

    public DistributeLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DistributeLockAspect.class);

    @Around("@annotation(com.lcl.nft.lock.DistributeLock)")
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        Object response = null;
        Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);

        String key = distributeLock.key();
        if(DistributeLockConstant.NONE_KEY.equals(key)) {
            if(DistributeLockConstant.NONE_KEY.equals(distributeLock.keyExpression())) {
                throw new DistributeLockException("no lock key found ...");
            }
            SpelExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(distributeLock.keyExpression());

            EvaluationContext context = new StandardEvaluationContext();
            // 获取参数
            Object[] args = proceedingJoinPoint.getArgs();
            // 获取运行时的参数名称
            StandardReflectionParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            // 将参数绑定到context中
            if(parameterNames != null) {
                for (int i = 0; i < parameterNames.length; i++) {
                    context.setVariable(parameterNames[i], args[i]);
                }
                key = String.valueOf(expression.getValue(context));
            }
        }
        String scene = distributeLock.scene();
        String lockKey = scene + "#" + key;

        int expireTime = distributeLock.expireTime();
        int waitTime = distributeLock.waitTime();
        RLock rLock = redissonClient.getFairLock(lockKey);
        boolean lockResult = false;
        if(waitTime == DistributeLockConstant.DEFAULT_WAIT_TIME) {
            if(expireTime == DistributeLockConstant.DEFAULT_EXPIRE_TIME) {
                LOG.info("log for key : {}", lockKey);
                rLock.lock();
            } else {
                LOG.info("log for key : {} , expire : {} ", lockKey, expireTime);
                rLock.lock(expireTime, TimeUnit.MILLISECONDS);
            }
        } else {
            if(expireTime == DistributeLockConstant.DEFAULT_EXPIRE_TIME) {
                LOG.info("log for key : {} , wait : {}", lockKey, waitTime);
                lockResult = rLock.tryLock(waitTime, TimeUnit.MILLISECONDS);
            } else {
                LOG.info("log for key : {} , expire : {} , wait : {}", lockKey, expireTime, waitTime);
                rLock.tryLock(waitTime, expireTime, TimeUnit.MILLISECONDS);
            }
        }

        if(!lockResult) {
            LOG.warn("lock failed for key : {} , expire : {} ", lockKey, expireTime);
            throw new DistributeLockException("acquire lock failed... key : " + lockKey);
        }

        try {
            LOG.info("lock success for key : {}, expire : {} ", lockKey, expireTime);
            response = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            rLock.unlock();
            LOG.info("unlock for key : {} , expire : {} ", lockKey, expireTime);
        }
        return response;
    }
}
