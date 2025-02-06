package com.lcl.nft.user.domain.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcl.nft.user.domain.entity.User;
import com.lcl.nft.user.domain.entity.UserOperateStream;
import com.lcl.nft.user.infrastructure.mapper.UserOperateStreamMapper;
import com.lcl.nft.api.user.constant.UserOperateTypeEnum;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户操作流水表 服务类
 * @Author conglongli
 * @date 2025/1/19 01:03
 */
@Service
public class UserOperateStreamService extends ServiceImpl<UserOperateStreamMapper, UserOperateStream> {

    public Long insertStream(User user, UserOperateTypeEnum type) {
        UserOperateStream stream = new UserOperateStream();
        stream.setUserId(String.valueOf(user.getId()));
        stream.setOperateTime(new Date());
        stream.setType(type.name());
        stream.setParam(JSON.toJSONString(user));
        boolean result = save(stream);
        if (result) {
            return stream.getId();
        }
        return null;
    }
}
