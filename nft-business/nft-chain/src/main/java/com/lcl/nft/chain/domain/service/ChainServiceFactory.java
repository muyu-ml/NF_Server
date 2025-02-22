package com.lcl.nft.chain.domain.service;

import com.lcl.nft.api.chain.constant.ChainType;
import com.lcl.nft.base.utils.BeanNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 链服务工厂
 * @Author conglongli
 * @date 2025/2/18 22:54
 */
@Service
public class ChainServiceFactory {

    @Autowired
    private final Map<String, ChainService> chainServiceMap = new ConcurrentHashMap<String, ChainService>();

    public ChainService get(ChainType chainType) {
        String beanName = BeanNameUtils.getBeanName(chainType.name(), "ChainService");

        //组装出beanName，并从map中获取对应的bean
        ChainService service = chainServiceMap.get(beanName);

        if (service != null) {
            return service;
        } else {
            throw new UnsupportedOperationException(
                    "No ChainService Found With chainType : " + chainType);
        }
    }
}

