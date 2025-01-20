package com.lcl.nft.user.domain.service.impl;

import com.lcl.nft.user.domain.service.AuthService;

/**
 * Mock的认证服务
 * @Author conglongli
 * @date 2025/1/19 01:01
 */
public class MockAuthServiceImpl implements AuthService {
    @Override
    public boolean checkAuth(String realName, String idCard) {
        return true;
    }
}
