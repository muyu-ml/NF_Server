package com.lcl.ntf.api.chain.service;

import com.lcl.ntf.api.chain.request.ChainProcessRequest;
import com.lcl.ntf.api.chain.response.ChainProcessResponse;
import com.lcl.ntf.api.chain.response.data.ChainCreateData;
import com.lcl.ntf.api.chain.response.data.ChainOperationData;

/**
 * @Author conglongli
 * @date 2025/1/15 01:54
 */
public interface ChainFacadeService {

    /**
     * 创建链账户
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
}
