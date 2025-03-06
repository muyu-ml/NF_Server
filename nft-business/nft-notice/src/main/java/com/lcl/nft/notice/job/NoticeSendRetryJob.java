package com.lcl.nft.notice.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcl.nft.notice.domain.constant.NoticeState;
import com.lcl.nft.notice.domain.entity.Notice;
import com.lcl.nft.notice.domain.service.NoticeService;
import com.lcl.nft.sms.SmsService;
import com.lcl.nft.sms.response.SmsSendResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author conglongli
 * @date 2025/3/6 16:37
 */
@Component
public class NoticeSendRetryJob {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SmsService smsService;

    private static final int PAGE_SIZE = 100;

    private static final Logger LOG = LoggerFactory.getLogger(NoticeSendRetryJob.class);

    @XxlJob("noticeSendRetryJob")
    public ReturnT<String> execute() {

        int currentPage = 1;
        Page<Notice> page = noticeService.pageQueryForRetry(currentPage, PAGE_SIZE);

        page.getRecords().forEach(this::executeSingle);

        while (page.hasNext()) {
            currentPage++;
            page = noticeService.pageQueryForRetry(currentPage, PAGE_SIZE);
            page.getRecords().forEach(this::executeSingle);
        }

        return ReturnT.SUCCESS;
    }

    private void executeSingle(Notice notice) {
        LOG.info("start to execute notice retry , noticeId is {}", notice.getId());

        SmsSendResponse result = smsService.sendMsg(notice.getTargetAddress(), notice.getNoticeContent());
        if (result.getSuccess()) {
            notice.setState(NoticeState.SUCCESS);
            notice.setSendSuccessTime(new Date());
            notice.setLockVersion(1);
        } else {
            notice.setState(NoticeState.FAILED);
            notice.setLockVersion(1);
            notice.addExtendInfo("executeResult", JSON.toJSONString(result));
        }
        noticeService.updateById(notice);
    }
}
