package com.lcl.nft.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Author conglongli
 * @date 2025/1/11 22:47
 */
public class TokenFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    public static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    private RedissonClient redissonClient;

    public TokenFilter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

            // 从请求头中获取Token
            String token = httpServletRequest.getHeader("Authorization");

            if(token == null || "null".equals(token) || "undefined".equals(token)) {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write("No Token Found ...");
                logger.error("no token found in header , pls check!");
                return;
            }

            // 校验 token 有效性
            boolean isValid = checkTokenValidity(token);

            if (!isValid){
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write("Invalid or expired token");
                logger.error("token validate failed , pls check!");
                return;
            }

            // token有效，继续执行其他过滤器
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            tokenThreadLocal.remove();
        }
    }


    private boolean checkTokenValidity(String token) {
        String luaScript = """
                local value = redis.call('GET', KEYS[1])
                redis.call('DEL', keys[1])
                return value
                """;
        // 在 6.2.3 上可以直接使用命令 GETDEL 命令
//        String value = (String) redisTemplate.opsForValue().getAndDelete(token);
        String result = redissonClient.getScript().eval(RScript.Mode.READ_WRITE,
                luaScript,
                RScript.ReturnType.STATUS,
                Arrays.asList(token));
        tokenThreadLocal.set(result);
        return result != null;
    }
}
