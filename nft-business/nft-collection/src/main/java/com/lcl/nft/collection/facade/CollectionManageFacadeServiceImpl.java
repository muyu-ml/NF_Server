package com.lcl.nft.collection.facade;

import com.alibaba.fastjson.JSON;
import com.lcl.nft.api.chain.constant.ChainOperateBizTypeEnum;
import com.lcl.nft.api.chain.request.ChainProcessRequest;
import com.lcl.nft.api.chain.service.ChainFacadeService;
import com.lcl.nft.api.collection.constant.CollectionInventoryModifyType;
import com.lcl.nft.api.collection.model.CollectionVO;
import com.lcl.nft.api.collection.request.*;
import com.lcl.nft.api.collection.response.CollectionChainResponse;
import com.lcl.nft.api.collection.response.CollectionInventoryModifyResponse;
import com.lcl.nft.api.collection.response.CollectionModifyResponse;
import com.lcl.nft.api.collection.response.CollectionRemoveResponse;
import com.lcl.nft.api.collection.service.CollectionManageFacadeService;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.entity.convertor.CollectionConvertor;
import com.lcl.nft.collection.domain.response.CollectionInventoryResponse;
import com.lcl.nft.collection.domain.service.CollectionService;
import com.lcl.nft.collection.domain.service.impl.redis.CollectionInventoryRedisService;
import com.lcl.nft.collection.exception.CollectionException;
import com.lcl.nft.rpc.facade.Facade;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lcl.nft.collection.exception.CollectionErrorCode.COLLECTION_INVENTORY_UPDATE_FAILED;

/**
 * 藏品管理服务
 * 藏品管理服务
 * @Author conglongli
 * @date 2025/2/26 17:16
 */
@DubboService(version = "1.0.0")
public class CollectionManageFacadeServiceImpl implements CollectionManageFacadeService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionManageFacadeServiceImpl.class);

    @Autowired
    private ChainFacadeService chainFacadeService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private CollectionInventoryRedisService collectionInventoryRedisService;

    @Override
    @Facade
    public CollectionChainResponse create(CollectionCreateRequest request) {
        Collection collection = collectionService.create(request);
        ChainProcessRequest chainProcessRequest = new ChainProcessRequest();
        chainProcessRequest.setIdentifier(request.getIdentifier());
        chainProcessRequest.setClassId(String.valueOf(collection.getId()));
        chainProcessRequest.setClassName(request.getName());
        chainProcessRequest.setBizType(ChainOperateBizTypeEnum.COLLECTION.name());
        chainProcessRequest.setBizId(collection.getId().toString());
        var chainRes = chainFacadeService.chain(chainProcessRequest);
        CollectionChainResponse response = new CollectionChainResponse();
        if (!chainRes.getSuccess()) {
            response.setSuccess(false);
            return response;
        }
        response.setSuccess(true);
        response.setCollectionId(collection.getId());
        return response;
    }

    @Override
    public CollectionRemoveResponse remove(CollectionRemoveRequest request) {
        CollectionRemoveResponse response = new CollectionRemoveResponse();
        Boolean result = collectionService.remove(request);

        if (result) {
            InventoryRequest inventoryRequest = new InventoryRequest();
            inventoryRequest.setCollectionId(request.getCollectionId().toString());
            collectionInventoryRedisService.invalid(inventoryRequest);
        }

        response.setSuccess(result);
        response.setCollectionId(request.getCollectionId());
        return response;
    }

    @Override
    public CollectionModifyResponse modifyInventory(CollectionModifyInventoryRequest request) {
        CollectionModifyResponse response = new CollectionModifyResponse();
        response.setCollectionId(request.getCollectionId());

        CollectionInventoryModifyResponse modifyResponse = collectionService.modifyInventory(request);

        if (!modifyResponse.getSuccess()) {
            response.setSuccess(false);
            response.setResponseCode(COLLECTION_INVENTORY_UPDATE_FAILED.getCode());
            response.setResponseMessage(COLLECTION_INVENTORY_UPDATE_FAILED.getMessage());
            return response;
        }

        if (modifyResponse.getModifyType() == CollectionInventoryModifyType.UNMODIFIED) {
            response.setSuccess(true);
            return response;
        }

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setCollectionId(request.getCollectionId().toString());
        inventoryRequest.setIdentifier(request.getIdentifier());
        inventoryRequest.setInventory(modifyResponse.getQuantityModified().intValue());
        CollectionInventoryResponse inventoryResponse;
        if (modifyResponse.getModifyType() == CollectionInventoryModifyType.INCREASE) {
            inventoryResponse = collectionInventoryRedisService.increase(inventoryRequest);
        } else {
            inventoryResponse = collectionInventoryRedisService.decrease(inventoryRequest);
        }

        if (!inventoryResponse.getSuccess()) {
            logger.error("modify inventory failed : " + JSON.toJSONString(inventoryResponse));
            throw new CollectionException(COLLECTION_INVENTORY_UPDATE_FAILED);
        }

        response.setSuccess(true);
        return response;
    }

    @Override
    public CollectionModifyResponse modifyPrice(CollectionModifyPriceRequest request) {
        Boolean result = collectionService.modifyPrice(request);
        CollectionModifyResponse response = new CollectionModifyResponse();
        response.setSuccess(result);
        response.setCollectionId(request.getCollectionId());
        return response;
    }

    @Override
    public PageResponse<CollectionVO> pageQuery(CollectionPageQueryRequest request) {
        PageResponse<Collection> colletionPage = collectionService.pageQueryByState(request.getKeyword(), request.getState(), request.getCurrentPage(), request.getPageSize());
        return PageResponse.of(CollectionConvertor.INSTANCE.mapToVo(colletionPage.getDatas()), colletionPage.getTotal(), colletionPage.getPageSize(), request.getCurrentPage());
    }

}

