package com.lcl.nft.collection.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcl.nft.api.collection.request.CollectionCreateRequest;
import com.lcl.nft.api.collection.request.CollectionModifyInventoryRequest;
import com.lcl.nft.api.collection.request.CollectionModifyPriceRequest;
import com.lcl.nft.api.collection.request.CollectionRemoveRequest;
import com.lcl.nft.api.collection.response.CollectionInventoryModifyResponse;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.collection.domain.entity.Collection;
import com.lcl.nft.collection.domain.response.CollectionConfirmSaleResponse;
import com.lcl.nft.collection.facade.request.CollectionCancelSaleRequest;
import com.lcl.nft.collection.facade.request.CollectionConfirmSaleRequest;
import com.lcl.nft.collection.facade.request.CollectionTrySaleRequest;

/**
 * 藏品服务
 * @Author conglongli
 * @date 2025/2/27 10:08
 */
public interface CollectionService extends IService<Collection> {
    /**
     * 创建
     *
     * @param request
     * @return
     */
    public Collection create(CollectionCreateRequest request);

    /**
     * 更新库存
     *
     * @param request
     * @return
     */
    public CollectionInventoryModifyResponse modifyInventory(CollectionModifyInventoryRequest request);

    /**
     * 更新价格
     *
     * @param request
     * @return
     */
    public Boolean modifyPrice(CollectionModifyPriceRequest request);

    /**
     * 下架
     *
     * @param request
     * @return
     */
    public Boolean remove(CollectionRemoveRequest request);

    /**
     * 尝试售卖
     *
     * @param request
     * @return
     */
    public Boolean trySale(CollectionTrySaleRequest request);

    /**
     * 尝试售卖-无hint版
     *
     * @param request
     * @return
     */
    public Boolean trySaleWithoutHint(CollectionTrySaleRequest request);

    /**
     * 取消售卖
     *
     * @param request
     * @return
     */
    public Boolean cancelSale(CollectionCancelSaleRequest request);

    /**
     * 确认售卖
     *
     * @param request
     * @return
     */
    public CollectionConfirmSaleResponse confirmSale(CollectionConfirmSaleRequest request);

    /**
     * 查询
     *
     * @param collectionId
     * @return
     */
    public Collection queryById(Long collectionId);

    /**
     * 分页查询
     *
     * @param keyWord
     * @param state
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageResponse<Collection> pageQueryByState(String keyWord, String state, int currentPage, int pageSize);
}
