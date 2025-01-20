package com.lcl.nft.api.user.service;

import com.lcl.nft.api.user.request.UserRegisterRequest;
import com.lcl.nft.api.user.response.UserOperatorResponse;

/**
 * @Author conglongli
 * @date 2025/1/15 01:54
 */
public interface UserManageFacadeService {

    /**
     * 管理用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    UserOperatorResponse registerAdmin(UserRegisterRequest userRegisterRequest);

    /**
     * 用户冻结
     *
     * @param userId
     * @return
     */
    UserOperatorResponse freeze(Long userId);

    /**
     * 用户解冻
     *
     * @param userId
     * @return
     */
    UserOperatorResponse unfreeze(Long userId);

}

