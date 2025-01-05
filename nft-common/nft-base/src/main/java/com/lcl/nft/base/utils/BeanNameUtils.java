package com.lcl.nft.base.utils;

import com.google.common.base.CaseFormat;

/**
 * @Author conglongli
 * @date 2025/1/6 01:22
 */
public class BeanNameUtils {

    /**
     * 把一个策略名称转换成beanName
     * <pre>
     *     如 wechat ，MessageService -> wechatMessageService
     * </pre>
     * @param strategyName
     * @param serviceName
     * @return
     */
    public static String getBeanName(String strategyName, String serviceName) {
        // 将服务转换成小写字母开头的驼峰形式，如A_BCD 转成 aBcd
        return CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(strategyName) + serviceName;
    }
}
