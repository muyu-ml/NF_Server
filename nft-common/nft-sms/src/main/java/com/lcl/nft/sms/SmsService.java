package com.lcl.nft.sms;

import com.lcl.nft.sms.response.SmsSendResponse;

/**
 * 短信服务
 * @Author conglongli
 * @date 2025/1/14 17:07
 */
public interface SmsService {
    SmsSendResponse sendMsg(String phoneNumber, String code);
}
