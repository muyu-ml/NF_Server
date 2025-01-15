package com.lcl.ntf.api.chain.request;

import com.lcl.nft.base.request.BaseRequest;
import lombok.*;

/**
 * 链处理参数
 * @Author conglongli
 * @date 2025/1/15 01:54
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChainQueryRequest extends BaseRequest {

    /**
     * 操作id
     */
    private String operationId;

    /**
     * 操作信息的主键 ID
     */
    private String operationInfoId;

}
