package com.lcl.nft.order.domain.validator;

import com.lcl.nft.api.goods.model.BaseGoodsVO;
import com.lcl.nft.api.goods.service.GoodsFacadeService;
import com.lcl.nft.api.order.request.OrderCreateRequest;
import com.lcl.nft.order.domain.exception.OrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lcl.nft.api.order.constant.OrderErrorCode.GOODS_NOT_AVAILABLE;
import static com.lcl.nft.api.order.constant.OrderErrorCode.GOODS_PRICE_CHANGED;

/**
 * 商品校验器
 * @Author conglongli
 * @date 2025/3/15 11:18
 */
@Component
public class GoodsValidator extends BaseOrderCreateValidator {

    @Autowired
    private GoodsFacadeService goodsFacadeService;

    @Override
    protected void doValidate(OrderCreateRequest request) throws OrderException {
        BaseGoodsVO baseGoodsVO = goodsFacadeService.getGoods(request.getGoodsId(), request.getGoodsType());

        if (!baseGoodsVO.available()) {
            throw new OrderException(GOODS_NOT_AVAILABLE);
        }

        if (baseGoodsVO.getPrice().compareTo(request.getItemPrice()) != 0) {
            throw new OrderException(GOODS_PRICE_CHANGED);
        }
    }
}
