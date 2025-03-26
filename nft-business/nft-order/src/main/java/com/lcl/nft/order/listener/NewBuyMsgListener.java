package com.lcl.nft.order.listener;

import com.alibaba.fastjson2.JSON;
import com.lcl.nft.api.order.OrderFacadeService;
import com.lcl.nft.api.order.request.OrderCreateAndConfirmRequest;
import com.lcl.nft.api.order.request.OrderCreateRequest;
import com.lcl.nft.api.order.response.OrderResponse;
import com.lcl.nft.api.user.constant.UserType;
import com.lcl.nft.mq.param.MessageBody;
import com.lcl.nft.order.domain.validator.OrderCreateValidator;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Consumer;

/**
 * @Author conglongli
 * @date 2025/3/23 00:11
 */
@Component
@Slf4j
public class NewBuyMsgListener {

    @Autowired
    private OrderFacadeService orderFacadeService;

    @Autowired
    private OrderCreateValidator orderValidatorChain;

    @Bean
    Consumer<Message<MessageBody>> newBuy() {
        return msg -> {
            String messageId = msg.getHeaders().get("ROCKET_MQ_MESSAGE_ID", String.class);
            String tag = msg.getHeaders().get("ROCKET_TAGS", String.class);
            OrderCreateRequest orderCreateRequest = JSON.parseObject(msg.getPayload().getBody(), OrderCreateRequest.class);
            log.info("Received NewBuy Message messageId:{},orderCreateRequest:{}，tag:{}", messageId, orderCreateRequest, tag);

            OrderCreateAndConfirmRequest orderCreateAndConfirmRequest = new OrderCreateAndConfirmRequest();
            BeanUtils.copyProperties(orderCreateRequest, orderCreateAndConfirmRequest);
            orderCreateAndConfirmRequest.setOperator(UserType.PLATFORM.name());
            orderCreateAndConfirmRequest.setOperatorType(UserType.PLATFORM);
            orderCreateAndConfirmRequest.setOperateTime(new Date());

            OrderResponse orderResponse = orderFacadeService.createAndConfirm(orderCreateAndConfirmRequest);
            Assert.isTrue(orderResponse.getSuccess(), "create order failed");
        };
    }
}
