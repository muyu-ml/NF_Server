package com.lcl.ntf.api.chain.response.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author conglongli
 * @date 2025/1/15 01:54
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChainOperationData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作编号
     */
    private String operationId;

}
