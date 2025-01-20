package com.lcl.nft.api.user.request;

import com.lcl.nft.base.request.BaseRequest;
import com.lcl.nft.api.user.request.condition.UserIdQueryCondition;
import com.lcl.nft.api.user.request.condition.UserPhoneAndPasswordQueryCondition;
import com.lcl.nft.api.user.request.condition.UserPhoneQueryCondition;
import com.lcl.nft.api.user.request.condition.UserQueryCondition;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 00:42
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseRequest {

    private UserQueryCondition userQueryCondition;

    public UserQueryRequest(Long userId) {
        UserIdQueryCondition userIdQueryCondition = new UserIdQueryCondition();
        userIdQueryCondition.setUserId(userId);
        this.userQueryCondition = userIdQueryCondition;
    }

    public UserQueryRequest(String telephone) {
        UserPhoneQueryCondition userPhoneQueryCondition = new UserPhoneQueryCondition();
        userPhoneQueryCondition.setTelephone(telephone);
        this.userQueryCondition = userPhoneQueryCondition;
    }

    public UserQueryRequest(String telephone, String password) {
        UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition = new UserPhoneAndPasswordQueryCondition();
        userPhoneAndPasswordQueryCondition.setTelephone(telephone);
        userPhoneAndPasswordQueryCondition.setPassword(password);
        this.userQueryCondition = userPhoneAndPasswordQueryCondition;
    }
}
