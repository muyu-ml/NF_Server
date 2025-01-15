package com.lcl.ntf.api.chain.model;

import com.lcl.ntf.api.chain.constant.ChainOperateBizTypeEnum;
import com.lcl.ntf.api.chain.constant.ChainOperateTypeEnum;
import com.lcl.ntf.api.chain.constant.ChainType;
import com.lcl.ntf.api.chain.response.data.ChainResultData;
import lombok.Getter;
import lombok.Setter;

/**
 * 链操作的方法体
 *
 * @Author conglongli
 * @date 2025/1/15 01:54
 */
@Setter
@Getter
public class ChainOperateBody {
    /**
     * 业务id
     */
    private String bizId;
    /**
     * 业务类型
     */
    private ChainOperateBizTypeEnum bizType;

    /**
     * 操作类型
     */
    private ChainOperateTypeEnum operateType;
    /**
     * 操作信息id
     */
    private Long operateInfoId;

    /**
     * 链类型
     */
    private ChainType chainType;

    /**
     * 具体业务数据
     */
    private ChainResultData chainResultData;
}
