package com.lcl.nft.sms;

import com.lcl.nft.lock.DistributeLock;
import com.lcl.nft.sms.response.SmsSendResponse;

/**
 * @Author conglongli
 * @date 2025/1/14 17:17
 */
public class MockSmsServiceImpl implements SmsService{

    @DistributeLock(scene = "SEND_SMS", keyExpression = "#phoneNumber")
    @Override
    public SmsSendResponse sendMsg(String phoneNumber, String code) {
        SmsSendResponse smsSendResponse = new SmsSendResponse();
        smsSendResponse.setSuccess(true);
        return smsSendResponse;
    }
}
