package com.lcl.nft.collection.listener;

import com.alibaba.fastjson2.JSON;
import com.lcl.nft.api.chain.model.ChainOperateBody;
import com.lcl.nft.api.chain.response.data.ChainResultData;
import com.lcl.nft.api.collection.constant.CollectionStateEnum;
import com.lcl.nft.api.collection.constant.HeldCollectionState;
import com.lcl.nft.api.collection.request.InventoryRequest;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.entity.HeldCollection;
import com.lcl.nft.collection.domain.request.HeldCollectionActiveRequest;
import com.lcl.nft.collection.domain.response.CollectionInventoryResponse;
import com.lcl.nft.collection.domain.service.CollectionService;
import com.lcl.nft.collection.domain.service.impl.HeldCollectionService;
import com.lcl.nft.collection.domain.service.impl.redis.CollectionInventoryRedisService;
import com.lcl.nft.collection.exception.CollectionException;
import com.lcl.nft.mq.param.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.function.Consumer;

import static com.lcl.nft.collection.exception.CollectionErrorCode.*;

/**
 * 链操作结果监听器
 * @Author conglongli
 * @date 2025/3/3 21:55
 */
@Slf4j
@Component
public class ChainOperateResultListener {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private HeldCollectionService heldCollectionService;

    @Autowired
    private CollectionInventoryRedisService collectionInventoryRedisService;

    @Bean
    Consumer<Message<MessageBody>> chain() {
        return msg -> {
            String messageId = msg.getHeaders().get("ROCKET_MQ_MESSAGE_ID", String.class);
            String tag = msg.getHeaders().get("ROCKET_TAGS", String.class);
            ChainOperateBody chainOperateBody = JSON.parseObject(msg.getPayload().getBody(), ChainOperateBody.class);
            log.info("Received Chain Message messageId:{},chainOperateBody:{}，tag:{}", messageId, JSON.toJSONString(chainOperateBody), tag);
            //更新相关业务表
            ChainResultData chainResultData = chainOperateBody.getChainResultData();
            //成功情况处理
            switch (chainOperateBody.getOperateType()) {
                case COLLECTION_CHAIN:
                    //藏品上链成功更新,只有一个txHash
                    Collection collection = collectionService.getById(chainOperateBody.getBizId());
                    if (null == collection) {
                        throw new CollectionException(COLLECTION_QUERY_FAIL);
                    }
                    //先写缓存，写成功再更新状态
                    InventoryRequest inventoryRequest = new InventoryRequest();
                    inventoryRequest.setCollectionId(collection.getId().toString());
                    inventoryRequest.setInventory(collection.getQuantity().intValue());
                    inventoryRequest.setIdentifier(collection.getId().toString());
                    CollectionInventoryResponse inventoryResponse = collectionInventoryRedisService.init(inventoryRequest);
                    if (!inventoryResponse.getSuccess()) {
                        throw new CollectionException(COLLECTION_INVENTORY_UPDATE_FAILED);
                    }
                    //更新状态
                    collection.setState(CollectionStateEnum.SUCCEED);
                    collection.setSyncChainTime(new Date());
                    collectionService.updateById(collection);
                    break;
                case COLLECTION_MINT:
                    HeldCollectionActiveRequest request = new HeldCollectionActiveRequest();
                    request.setHeldCollectionId(Long.valueOf(chainOperateBody.getBizId()));
                    request.setIdentifier(chainOperateBody.getOperateInfoId().toString());
                    request.setNftId(chainResultData.getNftId());
                    request.setTxHash(chainResultData.getTxHash());
                    boolean result = heldCollectionService.active(request);
                    Assert.isTrue(result, "active held collection failed");
                    break;
                case COLLECTION_TRANSFER:
                    //藏品铸造成功有nftId和txHash
                    HeldCollection transferCollection = heldCollectionService.queryByNftIdAndState(chainOperateBody.getBizId(),
                            HeldCollectionState.INIT.name());
                    if (null == transferCollection) {
                        throw new CollectionException(HELD_COLLECTION_QUERY_FAIL);
                    }
                    transferCollection.actived(chainResultData.getNftId(), chainResultData.getTxHash());
                    heldCollectionService.updateById(transferCollection);
                    break;
                case COLLECTION_DESTROY:
                    //藏品铸造成功有nftId和txHash
                    HeldCollection destroyCollection = heldCollectionService.queryByNftIdAndState(chainOperateBody.getBizId(),
                            HeldCollectionState.ACTIVED.name());
                    if (null == destroyCollection) {
                        throw new CollectionException(HELD_COLLECTION_QUERY_FAIL);
                    }
                    destroyCollection.destroy();
                    heldCollectionService.updateById(destroyCollection);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + chainOperateBody.getBizType().name());

            }
        };
    }

}
