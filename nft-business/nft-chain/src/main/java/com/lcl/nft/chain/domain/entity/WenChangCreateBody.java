package com.lcl.nft.chain.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author conglongli
 * @date 2025/2/18 14:48
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WenChangCreateBody extends WenChangRequestBody{

    @JSONField(name = "name")
    private String name;
}
