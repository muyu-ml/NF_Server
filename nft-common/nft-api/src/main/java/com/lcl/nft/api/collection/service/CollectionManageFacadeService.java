package com.lcl.nft.api.collection.service;

import com.lcl.nft.api.collection.request.*;
import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.api.collection.model.CollectionVO;
import com.lcl.nft.api.collection.response.CollectionChainResponse;
import com.lcl.nft.api.collection.response.CollectionModifyResponse;
import com.lcl.nft.api.collection.response.CollectionRemoveResponse;

/**
 * 藏品管理门面服务
 *
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public interface CollectionManageFacadeService {

    /**
     * 创建藏品
     *
     * @param request
     * @return
     */
    CollectionChainResponse create(CollectionCreateRequest request);


    /**
     * 藏品下架
     *
     * @param request
     * @return
     */
    CollectionRemoveResponse remove(CollectionRemoveRequest request);


    /**
     * 藏品库存修改
     *
     * @param request
     * @return
     */
    CollectionModifyResponse modifyInventory(CollectionModifyInventoryRequest request);

    /**
     * 藏品价格修改
     *
     * @param request
     * @return
     */
    CollectionModifyResponse modifyPrice(CollectionModifyPriceRequest request);

    /**
     * 藏品分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<CollectionVO> pageQuery(CollectionPageQueryRequest request);
}
