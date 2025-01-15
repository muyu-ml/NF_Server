package com.lcl.nft.mq.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息体
 * @Author conglongli
 * @date 2025/1/14 19:28
 */
@Data
@Accessors(chain = true)
public class MessageBody {
    /**
     * 幂等号
     */
    private String identifier;
    /**
     * 消息体
     */
    private String body;
}
