package com.lcl.nft.user.domain.service;

/**
 * 认证服务
 * @Author conglongli
 * @date 2025/1/19 00:05
 */
public interface AuthService {
    /**
     * 校验认证信息
     *
     * @param realName
     * @param idCard
     * @return
     */
    public boolean checkAuth(String realName, String idCard);
}
