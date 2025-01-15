package com.lcl.ntf.api.user.request;

import com.lcl.nft.base.request.BaseRequest;
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
public class UserPageQueryRequest extends BaseRequest {

    /**
     * 手机号关键字
     */
    private String keyWord;
    /**
     * 用户状态
     */
    private String state;
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 页面大小
     */
    private int pageSize;

}
