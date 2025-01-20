package com.lcl.nft.api.user.request;

import com.lcl.nft.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 01:22
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyRequest extends BaseRequest {
    @NotNull(message = "userId不能为空")
    private Long userId;
    private String nickName;
    private String password;
    private String profilePhotoUrl;
    private String telephone;

}
