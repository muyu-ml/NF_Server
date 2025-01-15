package com.lcl.nft.web.util;

import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.web.vo.MultiResult;

import static com.lcl.nft.base.response.ResponseCode.SUCCESS;

/**
 * @Author conglongli
 * @date 2025/1/9 18:26
 */
public class MultiResultConvertor {
    public static <T> MultiResult<T> convert(PageResponse<T> pageResponse) {
        MultiResult<T> multiResult = new MultiResult<T>(true, SUCCESS.name(), SUCCESS.name(), pageResponse.getDatas(), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
        return multiResult;
    }
}
