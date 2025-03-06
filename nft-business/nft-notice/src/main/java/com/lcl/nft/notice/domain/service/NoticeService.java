package com.lcl.nft.notice.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcl.nft.base.exception.BizException;
import com.lcl.nft.notice.domain.constant.NoticeState;
import com.lcl.nft.notice.domain.constant.NoticeType;
import com.lcl.nft.notice.domain.entity.Notice;
import com.lcl.nft.notice.infrastructure.mapper.NoticeMapper;

import static com.lcl.nft.base.exception.BizErrorCode.NOTICE_SAVE_FAILED;

/**
 * 通知服务
 * @Author conglongli
 * @date 2025/3/5 14:39
 */
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

    private static final String SMS_NOTICE_TITLE = "验证码";

    public Page<Notice> pageQueryForRetry(int currentPage, int pageSize) {
        Page<Notice> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("state", NoticeState.INIT.name(), NoticeState.FAILED);
        queryWrapper.orderBy(true, true, "gmt_create");
        return this.page(page, queryWrapper);
    }

    public Notice saveCaptcha(String telephone, String captcha) {
        Notice notice = Notice.builder().noticeTitle(SMS_NOTICE_TITLE).noticeContent(captcha)
                .noticeType(NoticeType.SMS).targetAddress(telephone).state(NoticeState.INIT).build();
        boolean saveResult = this.save(notice);
        if(!saveResult) {
            throw new BizException(NOTICE_SAVE_FAILED);
        }
        return notice;
    }
}
