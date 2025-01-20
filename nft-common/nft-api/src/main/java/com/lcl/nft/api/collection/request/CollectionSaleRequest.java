package com.lcl.nft.api.collection.request;

import com.lcl.nft.api.collection.constant.CollectionEvent;
import com.lcl.nft.api.collection.constant.CollectionSaleBizType;
import com.lcl.nft.api.order.request.OrderCreateAndConfirmRequest;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CollectionSaleRequest extends BaseCollectionRequest {

    /**
     * 藏品名称
     */
    private String name;

    /**
     * 藏品封面
     */
    private String cover;

    /**
     * 藏品类目id
     */
    private String classId;

    /**
     * 购入价格
     */
    private BigDecimal purchasePrice;

    /**
     * '持有人id'
     */
    private String userId;

    /**
     * 销售数量
     */
    private Long quantity;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 业务类型
     * @see CollectionSaleBizType
     */
    private String bizType;


    @Override
    public CollectionEvent getEventType() {
        return CollectionEvent.SALE;
    }

    public CollectionSaleRequest(OrderCreateAndConfirmRequest orderCreateAndConfirmRequest) {
        this.userId = orderCreateAndConfirmRequest.getBuyerId();
        this.quantity = (long)orderCreateAndConfirmRequest.getItemCount();
        super.setCollectionId(Long.valueOf(orderCreateAndConfirmRequest.getGoodsId()));
        super.setIdentifier(orderCreateAndConfirmRequest.getOrderId());
    }
}
