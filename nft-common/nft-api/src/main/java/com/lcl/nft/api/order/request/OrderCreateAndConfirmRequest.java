package com.lcl.nft.api.order.request;

import com.lcl.nft.api.order.constant.TradeOrderEvent;
import com.lcl.nft.api.user.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Getter
@Setter
public class OrderCreateAndConfirmRequest extends OrderCreateRequest {

    /**
     * 操作时间
     */
    @NotNull(message = "operateTime 不能为空")
    private Date operateTime;

    /**
     * 操作人
     */
    @NotNull(message = "operator 不能为空")
    private String operator;

    /**
     * 操作人类型
     */
    @NotNull(message = "operatorType 不能为空")
    private UserType operatorType;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.CREATE_AND_CONFIRM;
    }
}
