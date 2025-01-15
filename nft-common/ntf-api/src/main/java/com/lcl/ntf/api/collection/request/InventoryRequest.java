package com.lcl.ntf.api.collection.request;

import com.lcl.nft.base.request.BaseRequest;
import com.lcl.ntf.api.order.request.OrderCreateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
@NoArgsConstructor
public class InventoryRequest extends BaseRequest {

    /**
     * 藏品ID
     */
    @NotNull(message = "collectionId is null")
    private String collectionId;

    /**
     * 唯一标识
     */
    private String identifier;

    /**
     * 库存数量
     */
    private Integer inventory;

    public InventoryRequest(OrderCreateRequest orderCreateRequest) {
        this.collectionId = orderCreateRequest.getGoodsId();
        this.identifier = orderCreateRequest.getOrderId();
        this.inventory = orderCreateRequest.getItemCount();
    }
}
