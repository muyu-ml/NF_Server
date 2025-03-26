package com.lcl.nft.order.domain.validator;

import com.lcl.nft.api.goods.model.BaseGoodsInventoryVO;
import com.lcl.nft.api.order.request.OrderCreateRequest;
import com.lcl.nft.order.domain.exception.OrderException;
import com.lcl.nft.order.wrapper.InventoryWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lcl.nft.api.order.constant.OrderErrorCode.INVENTORY_NOT_ENOUGH;

/**
 * 库存校验器
 * @Author conglongli
 * @date 2025/3/15 11:14
 */
@Component
public class StockValidator extends BaseOrderCreateValidator {

    @Autowired
    private InventoryWrapperService inventoryWrapperService;

    @Override
    public void doValidate(OrderCreateRequest request) throws OrderException {
        BaseGoodsInventoryVO goodsInventoryVO = inventoryWrapperService.queryInventory(request);

        if (goodsInventoryVO == null) {
            throw new OrderException(INVENTORY_NOT_ENOUGH);
        }

        if (goodsInventoryVO.getInventory() == 0) {
            throw new OrderException(INVENTORY_NOT_ENOUGH);
        }

        if (goodsInventoryVO.getQuantity() < request.getItemCount()) {
            throw new OrderException(INVENTORY_NOT_ENOUGH);
        }

        if (goodsInventoryVO.getInventory() < request.getItemCount()) {
            throw new OrderException(INVENTORY_NOT_ENOUGH);
        }
    }
}

