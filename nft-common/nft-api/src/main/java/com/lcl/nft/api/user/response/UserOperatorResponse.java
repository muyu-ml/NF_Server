package com.lcl.nft.api.user.response;

import com.lcl.nft.base.response.BaseResponse;
import com.lcl.nft.api.user.response.data.UserInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @Author conglongli
 * @date 2025/1/15 01:52
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
