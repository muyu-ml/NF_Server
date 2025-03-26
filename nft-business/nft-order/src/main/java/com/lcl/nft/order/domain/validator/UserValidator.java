package com.lcl.nft.order.domain.validator;

import com.lcl.nft.api.order.request.OrderCreateRequest;
import com.lcl.nft.api.user.constant.UserRole;
import com.lcl.nft.api.user.constant.UserStateEnum;
import com.lcl.nft.api.user.request.UserQueryRequest;
import com.lcl.nft.api.user.response.UserQueryResponse;
import com.lcl.nft.api.user.response.data.UserInfo;
import com.lcl.nft.api.user.service.UserFacadeService;
import com.lcl.nft.order.domain.exception.OrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lcl.nft.api.order.constant.OrderErrorCode.*;

/**
 * 用户校验器
 * @Author conglongli
 * @date 2025/3/15 11:20
 */
@Component
public class UserValidator extends BaseOrderCreateValidator {

    @Autowired
    private UserFacadeService userFacadeService;

    @Override
    public void doValidate(OrderCreateRequest request) throws OrderException {
        String buyerId = request.getBuyerId();
        UserQueryRequest userQueryRequest = new UserQueryRequest(Long.valueOf(buyerId));
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(userQueryRequest);
        if (userQueryResponse.getSuccess() && userQueryResponse.getData() != null) {
            UserInfo userInfo = userQueryResponse.getData();
            if (userInfo.getUserRole() != null && !userInfo.getUserRole().equals(UserRole.CUSTOMER)) {
                throw new OrderException(BUYER_IS_PLATFORM_USER);
            }
            //判断买家状态
            if (userInfo.getState() != null && !userInfo.getState().equals(UserStateEnum.ACTIVE.name())) {
                throw new OrderException(BUYER_STATUS_ABNORMAL);
            }
            //判断买家状态
            if (userInfo.getState() != null && !userInfo.getCertification()) {
                throw new OrderException(BUYER_NOT_AUTH);
            }
        }
    }
}
