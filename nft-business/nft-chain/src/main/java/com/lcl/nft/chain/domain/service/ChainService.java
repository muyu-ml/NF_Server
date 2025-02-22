package com.lcl.nft.chain.domain.service;

import com.lcl.nft.api.chain.request.ChainProcessRequest;
import com.lcl.nft.api.chain.request.ChainQueryRequest;
import com.lcl.nft.api.chain.response.ChainProcessResponse;
import com.lcl.nft.api.chain.response.data.ChainCreateData;
import com.lcl.nft.api.chain.response.data.ChainOperationData;
import com.lcl.nft.api.chain.response.data.ChainResultData;
import com.lcl.nft.chain.domain.entity.ChainOperateInfo;

/**
 * 交易链服务
 * @Author conglongli
 * @date 2025/2/18 22:55
 */
public interface ChainService {

    /**
     * 创建交易链地址
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request);


    /**
     * 上链藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest request);

    /**
     * 铸造藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request);

    /**
     * 交易藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request);

    /**
     * 销毁藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request);

    /**
     * 查询上链交易结果
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainResultData> queryChainResult(ChainQueryRequest request);

    /**
     * 发消息
     *
     * @param chainOperateInfo
     * @param chainResultData
     */
    public void sendMsg(ChainOperateInfo chainOperateInfo, ChainResultData chainResultData);

}