package com.lcl.nft.chain.job;

import com.lcl.nft.api.chain.constant.ChainType;
import com.lcl.nft.api.chain.request.ChainQueryRequest;
import com.lcl.nft.api.chain.response.ChainProcessResponse;
import com.lcl.nft.api.chain.response.data.ChainResultData;
import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.base.exception.RepoErrorCode;
import com.lcl.nft.base.exception.SystemException;
import com.lcl.nft.chain.domain.constant.ChainOperateStateEnum;
import com.lcl.nft.chain.domain.entity.ChainOperateInfo;
import com.lcl.nft.chain.domain.service.ChainOperateInfoService;
import com.lcl.nft.chain.domain.service.ChainService;
import com.lcl.nft.chain.domain.service.ChainServiceFactory;
import com.lcl.nft.chain.infrastructure.exception.ChainErrorCode;
import com.lcl.nft.chain.infrastructure.exception.ChainException;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 链上处理任务
 * @Author conglongli
 * @date 2025/2/20 23:46
 */
@Component
public class ChainProcessJob {

    @Autowired
    private ChainOperateInfoService chainOperateInfoService;

    @Autowired
    private ChainServiceFactory chainServiceFactory;

    private static final int PAGE_SIZE = 5;

    private static final Logger LOG = LoggerFactory.getLogger(ChainProcessJob.class);

    @XxlJob("unFinishOperateExecute")
    public ReturnT<String> execute() {

        Long minId = chainOperateInfoService.queryMinIdByState(ChainOperateStateEnum.PROCESSING.name());

        List<ChainOperateInfo> chainOperateInfos = chainOperateInfoService.pageQueryOperateInfoByState(
                ChainOperateStateEnum.PROCESSING.name(), PAGE_SIZE, minId);

        chainOperateInfos.forEach(this::executeSingle);

        while (CollectionUtils.isNotEmpty(chainOperateInfos)) {
            minId = chainOperateInfos.stream().mapToLong(ChainOperateInfo::getId).max().orElse(0L);
            chainOperateInfos = chainOperateInfoService.pageQueryOperateInfoByState(ChainOperateStateEnum.PROCESSING.name()
                    , PAGE_SIZE, minId + 1);
            chainOperateInfos.forEach(this::executeSingle);
        }

        return ReturnT.SUCCESS;
    }

    private void executeSingle(ChainOperateInfo chainOperateInfo) {

        LOG.info("start to execute unfinish operate , id is {}", chainOperateInfo.getId());
        try {
            ChainService chainService = chainServiceFactory.get(ChainType.valueOf(chainOperateInfo.getChainType()));
            ChainQueryRequest query = new ChainQueryRequest();
            query.setOperationId(chainOperateInfo.getOutBizId());
            ChainProcessResponse<ChainResultData> chainProcessResponse = chainService.queryChainResult(query);
            if (!chainProcessResponse.getSuccess()) {
                throw new ChainException(ChainErrorCode.CHAIN_QUERY_FAIL);
            }
            ChainResultData chainResultData = chainProcessResponse.getData();
            //异常情况判断
            if (null == chainResultData) {
                throw new ChainException(ChainErrorCode.CHAIN_QUERY_FAIL);
            }
            if (!StringUtils.equals(chainResultData.getState(), ChainOperateStateEnum.SUCCEED.name())) {
                throw new BizException(ChainErrorCode.CHAIN_PROCESS_STATE_ERROR);
            }
            //成功情况处理
            if (StringUtils.equals(chainResultData.getState(), ChainOperateStateEnum.SUCCEED.name())) {
                //发送消息
                chainService.sendMsg(chainOperateInfo, chainResultData);
                //更新操作表状态
                //需要做核对，如果操作表状态成功，相应业务表状态处理中，需要核对出来
                boolean updateResult = chainOperateInfoService.updateResult(chainOperateInfo.getId(),
                        ChainOperateStateEnum.SUCCEED, null);
                if (!updateResult) {
                    throw new SystemException(RepoErrorCode.UPDATE_FAILED);
                }
            }
        } catch (Exception e) {
            LOG.error("start to execute unfinish operate error, id is {}, error is {}", chainOperateInfo.getId(), e);
        }
    }


}
