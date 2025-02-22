package com.lcl.nft.chain.domain.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.lcl.nft.api.chain.constant.ChainOperateTypeEnum;
import com.lcl.nft.api.chain.constant.ChainType;
import com.lcl.nft.api.chain.request.ChainProcessRequest;
import com.lcl.nft.api.chain.request.ChainQueryRequest;
import com.lcl.nft.api.chain.response.ChainProcessResponse;
import com.lcl.nft.api.chain.response.data.ChainCreateData;
import com.lcl.nft.api.chain.response.data.ChainOperationData;
import com.lcl.nft.api.chain.response.data.ChainResultData;
import com.lcl.nft.chain.domain.constant.ChainCodeEnum;
import com.lcl.nft.chain.domain.constant.ChainOperateStateEnum;
import com.lcl.nft.chain.domain.entity.ChainRequest;
import com.lcl.nft.chain.domain.response.ChainResponse;
import com.lcl.nft.chain.domain.service.AbstractChainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * mock链服务
 * @Author conglongli
 * @date 2025/2/19 00:18
 */
@Service("mockChainService")
public class MockChainServiceImpl extends AbstractChainService {

    @Override
    public ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request) {
        return new ChainProcessResponse.Builder().responseCode(ChainCodeEnum.SUCCESS.name()).data(
                new ChainCreateData(request.getIdentifier(), UUID.randomUUID().toString(), "mockBlockChainName",
                        ChainType.MOCK.name())).buildSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request) {
        return doPostExecute(request, ChainOperateTypeEnum.COLLECTION_MINT, chainRequest -> {
        });
    }

    @Override
    public ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest chainProcessRequest) {
        return doPostExecute(chainProcessRequest, ChainOperateTypeEnum.COLLECTION_CHAIN, chainRequest -> {
        });
    }

    @Override
    public ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request) {
        return doPostExecute(request, ChainOperateTypeEnum.COLLECTION_TRANSFER, chainRequest -> {
        });
    }

    @Override
    public ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request) {
        return doPostExecute(request, ChainOperateTypeEnum.COLLECTION_DESTROY, chainRequest -> {
        });
    }

    @Override
    public ChainProcessResponse<ChainResultData> queryChainResult(ChainQueryRequest request) {
        ChainProcessResponse<ChainResultData> response = new ChainProcessResponse<>();
        response.setSuccess(true);
        response.setResponseCode("200");
        response.setResponseMessage("SUCCESS");
        ChainResultData data = new ChainResultData();
        data.setTxHash(UUID.randomUUID().toString());
        data.setNftId("nftId");
        data.setState(ChainOperateStateEnum.SUCCEED.name());
        response.setData(data);
        return response;
    }

    @Override
    protected ChainResponse doPost(ChainRequest chainRequest) {
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setData(data);
        return chainResponse;
    }

    @Override
    protected ChainResponse doDelete(ChainRequest chainRequest) {
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setData(data);
        return chainResponse;
    }

    @Override
    protected ChainResponse doGetQuery(ChainRequest chainRequest) {
        ChainResponse chainResponse = new ChainResponse();
        chainResponse.setSuccess(true);
        JSONObject data = new JSONObject();
        data.put("success",true);
        data.put("chainType","mock");
        chainResponse.setData(data);
        return chainResponse;
    }

    @Override
    protected String chainType() {
        return ChainType.MOCK.name();
    }
}
