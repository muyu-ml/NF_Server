package com.lcl.nft.goods.facade.service;

import com.lcl.nft.api.collection.model.CollectionVO;
import com.lcl.nft.api.collection.service.CollectionFacadeService;
import com.lcl.nft.api.goods.constant.GoodsType;
import com.lcl.nft.api.goods.model.BaseGoodsVO;
import com.lcl.nft.api.goods.service.GoodsFacadeService;
import com.lcl.nft.base.response.SingleResponse;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author conglongli
 * @date 2025/3/5 11:04
 */
@DubboService(version = "1.0.0")
public class GoodsFacadeServiceImpl implements GoodsFacadeService {

    @DubboReference(version = "1.0.0")
    private CollectionFacadeService collectionFacadeService;

    @Override
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType) {
        return switch (goodsType) {
            case GoodsType.COLLECTION -> {
                SingleResponse<CollectionVO> response = collectionFacadeService.queryById(Long.valueOf(goodsId));
                if(response.getSuccess()) {
                    yield response.getData();
                }
                yield null;
            }
            default -> throw new UnsupportedOperationException("Unsupported GOODS TYPE");
        };
    }
}
