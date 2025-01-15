package com.lcl.ntf.api.order.request;

import com.lcl.ntf.api.user.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
@Setter
@Getter
public abstract class BaseOrderUpdateRequest extends BaseOrderRequest {

    /**
     * 订单id
     */
    @NotNull(message = "orderId 不能为空")
    private String orderId;

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
}
