package com.lcl.nft.api.user.request.condition;

import lombok.*;

/**
 * @Author conglongli
 * @date 2025/1/15 00:40
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserIdQueryCondition implements UserQueryCondition{

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
}
