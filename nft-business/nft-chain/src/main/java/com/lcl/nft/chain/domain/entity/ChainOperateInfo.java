package com.lcl.nft.chain.domain.entity;

import com.lcl.nft.chain.domain.constant.ChainOperateStateEnum;
import com.lcl.nft.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 链操作
 * @Author conglongli
 * @date 2025/2/18 14:43
 */
@Getter
@Setter
public class ChainOperateInfo extends BaseEntity {

    /**
     * 链类型
     */
    private String chainType;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 业务类型
     *
     * @see com.lcl.nft.api.chain.constant.ChainOperateBizTypeEnum
     */
    private String bizType;

    /**
     * 操作类型
     * @see com.lcl.nft.api.chain.constant.ChainOperateTypeEnum
     */
    private String operateType;

    /**
     * 状态
     */
    private ChainOperateStateEnum state;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 成功时间
     */
    private Date succeedTime;

    /**
     * 入参
     */
    private String param;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 外部业务id
     */
    private String outBizId;
}
