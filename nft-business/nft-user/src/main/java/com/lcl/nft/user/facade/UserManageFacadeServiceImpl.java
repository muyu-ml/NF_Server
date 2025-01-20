package com.lcl.nft.user.facade;

import com.lcl.nft.rpc.facade.Facade;
import com.lcl.nft.user.domain.service.UserService;
import com.lcl.ntf.api.user.request.UserRegisterRequest;
import com.lcl.ntf.api.user.response.UserOperatorResponse;
import com.lcl.ntf.api.user.service.UserManageFacadeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author conglongli
 * @date 2025/1/20 14:13
 */
@DubboService(version = "1.0.0")
public class UserManageFacadeServiceImpl implements UserManageFacadeService {

    @Autowired
    private UserService userService;

    @Override
    @Facade
    public UserOperatorResponse registerAdmin(UserRegisterRequest userRegisterRequest) {
        return userService.registerAdmin(userRegisterRequest.getTelephone(), userRegisterRequest.getPassword());
    }

    @Override
    public UserOperatorResponse freeze(Long userId) {
        return userService.freeze(userId);
    }

    @Override
    public UserOperatorResponse unfreeze(Long userId) {
        return userService.unfreeze(userId);
    }
}

