package com.lcl.nft.api.goods.service;

import com.lcl.nft.api.goods.constant.GoodsType;
import com.lcl.nft.api.goods.model.BaseGoodsVO;

/**
 * 商品服务
 *
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public interface GoodsFacadeService {

    /**
     * 获取商品
     *
     * @param goodsId
     * @param goodsType
     * @return
     */
    public BaseGoodsVO getGoods(String goodsId, GoodsType goodsType);

}
