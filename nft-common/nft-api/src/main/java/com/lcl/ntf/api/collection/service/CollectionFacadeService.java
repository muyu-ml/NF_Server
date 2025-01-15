package com.lcl.ntf.api.collection.service;


import com.lcl.nft.base.response.PageResponse;
import com.lcl.nft.base.response.SingleResponse;
import com.lcl.ntf.api.collection.model.CollectionInventoryVO;
import com.lcl.ntf.api.collection.model.CollectionVO;
import com.lcl.ntf.api.collection.model.HeldCollectionVO;
import com.lcl.ntf.api.collection.request.*;
import com.lcl.ntf.api.collection.response.CollectionDestroyResponse;
import com.lcl.ntf.api.collection.response.CollectionSaleResponse;
import com.lcl.ntf.api.collection.response.CollectionTransferResponse;

/**
 * 藏品门面服务
 *
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public interface CollectionFacadeService {

    /**
     * 藏品出售的try阶段，做库存预占用
     *
     * @param request
     * @return
     */
    CollectionSaleResponse trySale(CollectionSaleRequest request);

    /**
     * 藏品出售的try阶段，做库存预占用-无hint
     *
     * @param request
     * @return
     */
    CollectionSaleResponse trySaleWithoutHint(CollectionSaleRequest request);

    /**
     * 藏品出售的confirm阶段，做真正售出
     *
     * @param request
     * @return
     */
    CollectionSaleResponse confirmSale(CollectionSaleRequest request);

    /**
     * 藏品出售的cancel阶段，做库存退还
     *
     * @param request
     * @return
     */
    CollectionSaleResponse cancelSale(CollectionSaleRequest request);

    /**
     * 转移藏品
     *
     * @param request
     * @return
     */
    CollectionTransferResponse transfer(CollectionTransferRequest request);

    /**
     * 销毁藏品
     *
     * @param request
     * @return
     */
    CollectionDestroyResponse destroy(CollectionDestroyRequest request);

    /**
     * 根据Id查询藏品
     *
     * @param collectionId
     * @return
     */
    public SingleResponse<CollectionVO> queryById(Long collectionId);

    /**
     * 预扣减库存
     *
     * @param request 入参
     * @return
     */
    public SingleResponse<Boolean> preInventoryDeduct(InventoryRequest request);

    /**
     * 查询库存操作流水
     *
     * @param request
     * @return
     */
    public SingleResponse<String> getInventoryDecreaseLog(InventoryRequest request);

    /**
     * 查询藏品库存
     *
     * @param collectionId
     * @return
     */
    public SingleResponse<CollectionInventoryVO> queryInventory(Long collectionId);

    /**
     * 藏品分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<CollectionVO> pageQuery(CollectionPageQueryRequest request);

    /**
     * 持有藏品分页查询
     *
     * @param request
     * @return
     */
    public PageResponse<HeldCollectionVO> pageQueryHeldCollection(HeldCollectionPageQueryRequest request);

    /**
     * 根据id查询持有藏品
     *
     * @param heldCollectionId
     * @return
     */
    public SingleResponse<HeldCollectionVO> queryHeldCollectionById(Long heldCollectionId);

}
