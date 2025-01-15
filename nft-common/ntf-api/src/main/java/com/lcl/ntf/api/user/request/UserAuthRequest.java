package com.lcl.ntf.api.user.request;

import com.lcl.nft.base.request.BaseRequest;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 01:23
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest extends BaseRequest {

    private Long userId;
    private String realName;
    private String idCard;

}
