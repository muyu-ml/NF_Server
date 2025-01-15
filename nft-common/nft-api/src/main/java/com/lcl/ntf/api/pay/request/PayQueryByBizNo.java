package com.lcl.ntf.api.pay.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/1/15 10:35
 */
@Getter
@Setter
public class PayQueryByBizNo implements PayQueryCondition {

    private String bizNo;

    private String bizType;
}
