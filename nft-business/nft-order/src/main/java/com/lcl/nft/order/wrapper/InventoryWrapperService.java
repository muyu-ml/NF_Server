package com.lcl.nft.order.wrapper;

import com.lcl.nft.api.collection.model.CollectionInventoryVO;
import com.lcl.nft.api.collection.request.InventoryRequest;
import com.lcl.nft.api.collection.service.CollectionFacadeService;
import com.lcl.nft.api.goods.constant.GoodsType;
import com.lcl.nft.api.goods.model.BaseGoodsInventoryVO;
import com.lcl.nft.api.order.request.OrderCreateRequest;
import com.lcl.nft.base.response.SingleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author conglongli
 * @date 2025/3/22 23:55
 */
@Service
public class InventoryWrapperService {

    @Autowired
    private CollectionFacadeService collectionFacadeService;

    /**
     * 预扣减
     * @param orderCreateRequest
     * @return
     */
    public Boolean preDeduct(OrderCreateRequest orderCreateRequest) {
        GoodsType goodsType = orderCreateRequest.getGoodsType();
        return switch (goodsType) {
            case COLLECTION -> {
                InventoryRequest inventoryRequest = new InventoryRequest();
                inventoryRequest.setCollectionId(orderCreateRequest.getGoodsId());
                inventoryRequest.setIdentifier(orderCreateRequest.getOrderId());
                inventoryRequest.setInventory(orderCreateRequest.getItemCount());
                SingleResponse<Boolean> response = collectionFacadeService.preInventoryDeduct(inventoryRequest);
                if (response.getSuccess()) {
                    yield response.getData();
                }
                yield Boolean.FALSE;
            }
            default -> throw new UnsupportedOperationException("unsupport goods type");
        };
    }


    /**
     * 查询库存
     * @param orderCreateRequest
     * @return
     */
    public BaseGoodsInventoryVO queryInventory(OrderCreateRequest orderCreateRequest) {

        GoodsType goodsType = orderCreateRequest.getGoodsType();
        return switch (goodsType) {
            case COLLECTION -> {
                SingleResponse<CollectionInventoryVO> response = collectionFacadeService.queryInventory(Long.valueOf(orderCreateRequest.getGoodsId()));
                if (response.getSuccess()) {
                    yield response.getData();
                }
                yield null;
            }
            default -> throw new UnsupportedOperationException("unsupport goods type");
        };
    }
}
