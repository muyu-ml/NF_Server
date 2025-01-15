package com.lcl.ntf.api.user.request;

import com.lcl.nft.base.request.BaseRequest;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 01:25
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserActiveRequest extends BaseRequest {

    private Long userId;
    private String blockChainPlatform;
    private String blockChainUrl;

}
