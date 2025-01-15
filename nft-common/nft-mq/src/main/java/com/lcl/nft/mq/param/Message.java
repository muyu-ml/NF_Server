package com.lcl.nft.mq.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息
 * @Author conglongli
 * @date 2025/1/14 17:41
 */
@Data
@Accessors(chain = true)
public class Message {
    /**
     * 消息id
     */
    private String msgId;
    /**
     * 消息体
     */
    private String body;
}
