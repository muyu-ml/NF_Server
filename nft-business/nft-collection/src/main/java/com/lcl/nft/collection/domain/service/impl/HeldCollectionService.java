package com.lcl.nft.collection.domain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcl.nft.api.collection.constant.HeldCollectionState;
import com.lcl.nft.api.collection.model.HeldCollectionDTO;
import com.lcl.nft.api.collection.request.HeldCollectionPageQueryRequest;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.collection.domain.constant.HeldCollectionEventType;
import com.lcl.nft.collection.domain.entity.HeldCollection;
import com.lcl.nft.collection.domain.entity.convertor.HeldCollectionConvertor;
import com.lcl.nft.collection.domain.request.HeldCollectionActiveRequest;
import com.lcl.nft.collection.domain.request.HeldCollectionCreateRequest;
import com.lcl.nft.collection.domain.request.HeldCollectionDestroyRequest;
import com.lcl.nft.collection.domain.request.HeldCollectionTransferRequest;
import com.lcl.nft.collection.exception.CollectionException;
import com.lcl.nft.collection.infrastructure.mapper.HeldCollectionMapper;
import com.lcl.nft.mq.producer.StreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lcl.nft.collection.exception.CollectionErrorCode.*;

/**
 * @Author conglongli
 * @date 2025/2/27 10:18
 */
@Service
public class HeldCollectionService extends ServiceImpl<HeldCollectionMapper, HeldCollection> {

    @Autowired
    private StreamProducer streamProducer;

    public HeldCollection create(HeldCollectionCreateRequest request) {
        HeldCollection heldCollection = new HeldCollection();
        heldCollection.init(request);
        var saveResult = this.save(heldCollection);
        if(!saveResult) {
            throw new CollectionException(COLLECTION_SAVE_FAILED);
        }
        return heldCollection;
    }


    public Boolean active(HeldCollectionActiveRequest request) {
        HeldCollection heldCollection = getById(request.getHeldCollectionId());
        if (null == heldCollection) {
            throw new CollectionException(COLLECTION_QUERY_FAIL);
        }
        if(heldCollection.getState().equals(HeldCollectionState.ACTIVED.name())) {
            return true;
        }
        heldCollection.actived(request.getNftId(), request.getTxHash());
        boolean result = updateById(heldCollection);
        if(result) {
            sendMsg(heldCollection, request.getEventType());
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public HeldCollection transfer(HeldCollectionTransferRequest request) {
        // 先失效历史的持有数据
        HeldCollection oldHeldCollection = this.getById(request.getHeldCollectionId());
        if (oldHeldCollection == null) {
            throw new CollectionException(HELD_COLLECTION_QUERY_FAIL);
        }
        oldHeldCollection.inActived();
        var inActiveRes = this.updateById(oldHeldCollection);
        if(!inActiveRes) {
            throw new CollectionException(HELD_COLLECTION_SAVE_FAILED);
        }
        // 再初始化新的持有数据
        HeldCollection newHeldCollection = new HeldCollection();
        newHeldCollection.transfer(oldHeldCollection.getCollectionId(), oldHeldCollection.getSerialNo(),
                String.valueOf(request.getSellerId()), String.valueOf(request.getBuyerId()), oldHeldCollection.getNftId());
        var newHeldSaveResult = this.save(newHeldCollection);
        if(!newHeldSaveResult) {
            throw new CollectionException(HELD_COLLECTION_SAVE_FAILED);
        }
        return newHeldCollection;
    }

    public HeldCollection destroy(HeldCollectionDestroyRequest request) {
        // 查询持有数据
        HeldCollection heldCollection = this.getById(request.getHeldCollectionId());
        if(heldCollection == null) {
            throw new CollectionException(HELD_COLLECTION_QUERY_FAIL);
        }
        heldCollection.destroy();
        var saveResult = this.updateById(heldCollection);
        if(!saveResult) {
            throw new CollectionException(HELD_COLLECTION_SAVE_FAILED);
        }
        return heldCollection;
    }

    @Cached(name = ":held_collection:cache:id:", expire = 60, localExpire = 10, timeUnit = TimeUnit.MINUTES, cacheType = CacheType.BOTH, key = "#heldCollectionId", cacheNullValue = true)
    @CacheRefresh(refresh = 50, timeUnit = TimeUnit.MINUTES)
    public HeldCollection queryById(Long heldcollectionId) {
        return getById(heldcollectionId);
    }

    public HeldCollection queryByCollectionIdAndSerialNo(Long collectionId, String serialNo) {
        QueryWrapper<HeldCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("collection_id", collectionId);
        queryWrapper.eq("serial_no", serialNo);
        List<HeldCollection> heldCollections = list(queryWrapper);
        if(CollectionUtils.isEmpty(heldCollections)) {
            return null;
        }
        return heldCollections.get(0);
    }

    public HeldCollection queryByNftIdAndState(String nftId, String state) {
        QueryWrapper<HeldCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nft_id", nftId);
        queryWrapper.eq("state", state);
        List<HeldCollection> heldCollections = list(queryWrapper);
        if(StringUtils.isEmpty(heldCollections)) {
            return null;
        }
        return heldCollections.get(0);
    }

    public PageResponse<HeldCollection> pageQueryByState(HeldCollectionPageQueryRequest request) {
        Page<HeldCollection> page = new Page<>(request.getCurrentPage(), request.getPageSize());
        QueryWrapper<HeldCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", request.getUserId());
        queryWrapper.eq("name", request.getKeyword());
        if(request.getState() != null) {
            queryWrapper.eq("state", request.getState());
        }
        queryWrapper.orderBy(true, true, "gmt_create");
        Page<HeldCollection> collectionPage = this.page(page, queryWrapper);
        return PageResponse.of(collectionPage.getRecords(), (int)collectionPage.getTotal(), request.getPageSize(), request.getCurrentPage());
    }

    private boolean sendMsg(HeldCollection heldCollection, HeldCollectionEventType eventType) {
        HeldCollectionDTO heldCollectionDTO = HeldCollectionConvertor.INSTANCE.mapToDto(heldCollection);
        return streamProducer.send("heldCollection-out-0", eventType.name(), JSON.toJSONString(heldCollectionDTO));
    }
}
