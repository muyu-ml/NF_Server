package com.lcl.ntf.api.user.request;

import com.lcl.nft.base.request.BaseRequest;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 01:21
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest extends BaseRequest {

    private String telephone;

    private String inviteCode;

    private String password;

}
