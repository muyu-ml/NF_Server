package com.lcl.ntf.api.user.service;

import com.lcl.nft.base.response.PageResponse;
import com.lcl.ntf.api.user.request.*;
import com.lcl.ntf.api.user.response.UserOperatorResponse;
import com.lcl.ntf.api.user.response.UserQueryResponse;
import com.lcl.ntf.api.user.response.data.UserInfo;

/**
 * @Author conglongli
 * @date 2025/1/15 01:53
 */
public interface UserFacadeService {

    /**
     * 用户信息查询
     * @param userQueryRequest
     * @return
     */
    UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest);


    /**
     * 分页查询用户信息
     * @param userPageQueryRequest
     * @return
     */
    PageResponse<UserInfo> pageQuery(UserPageQueryRequest userPageQueryRequest);

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);

    /**
     * 用户信息修改
     * @param userModifyRequest
     * @return
     */
    UserOperatorResponse modify(UserModifyRequest userModifyRequest);

    /**
     * 用户实名认证
     * @param userAuthRequest
     * @return
     */
    UserOperatorResponse auth(UserAuthRequest userAuthRequest);

    /**
     * 用户激活
     * @param userActiveRequest
     * @return
     */
    UserOperatorResponse active(UserActiveRequest userActiveRequest);

}
