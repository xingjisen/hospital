package com.yygh.sms.receiver;

import com.rabbitmq.client.Channel;
import com.yygh.common.rabbit.constant.MqConst;
import com.yygh.sms.service.SmsService;
import com.yygh.vo.sms.SmsVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.sms.receiver
 * @Description MQ监听
 * @date 2024-11-13 23:17
 */
@Component
public class SmsReceiver {
    @Autowired
    private SmsService smsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_SMS_ITEM, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_SMS),
            key = {MqConst.ROUTING_SMS_ITEM}
    ))
    public void send(SmsVo smsVo, Message message, Channel channel) {
        smsService.send(smsVo);
    }

}
