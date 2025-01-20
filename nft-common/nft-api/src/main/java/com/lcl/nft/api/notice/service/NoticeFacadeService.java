package com.lcl.nft.api.notice.service;


import com.lcl.nft.api.notice.response.NoticeResponse;

/**
 * @Author conglongli
 * @date 2025/1/15 10:41
 */
public interface NoticeFacadeService {
    /**
     * 生成并发送短信验证码
     *
     * @param telephone
     * @return
     */
    public NoticeResponse generateAndSendSmsCaptcha(String telephone);
}
