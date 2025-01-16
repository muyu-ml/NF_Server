package com.lcl.nft.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.lcl.nft.auth.exception.AuthErrorCode;
import com.lcl.nft.auth.exception.AuthException;
import com.lcl.nft.web.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.lcl.nft.cache.constant.CacheConstant.CACHE_KEY_SEPARATOR;

/**
 * @Author conglongli
 * @date 2025/1/16 11:29
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("token")
public class TokenController {

    private static final String TOKEN_PREFIX = "token:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/get")
    public Result<String> get(@NotBlank String scene) {
        if (StpUtil.isLogin()) {
            String token = UUID.randomUUID().toString();
            String tokenKey = TOKEN_PREFIX + scene + CACHE_KEY_SEPARATOR + token;
            stringRedisTemplate.opsForValue().set(tokenKey, token, 30, TimeUnit.MINUTES);
            return Result.success(tokenKey);
        }
        throw new AuthException(AuthErrorCode.USER_NOT_LOGIN);
    }
}
