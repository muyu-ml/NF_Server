package com.lcl.nft.api.collection.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 持有藏品 DTO
 *
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
@ToString
public class HeldCollectionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 Id
     */
    private Long id;

    /**
     * '藏品id'
     */
    private Long collectionId;

    /**
     * '持有人id'
     */
    private String userId;

    /**
     * '状态'
     */
    private String state;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 业务类型
     */
    private String bizType;



}
