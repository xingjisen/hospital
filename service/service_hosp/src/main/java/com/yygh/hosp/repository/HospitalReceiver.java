package com.yygh.hosp.repository;

import com.rabbitmq.client.Channel;
import com.yygh.common.rabbit.constant.MqConst;
import com.yygh.common.rabbit.service.RabbitService;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.order.OrderMqVo;
import com.yygh.vo.sms.SmsVo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.repository
 * @Description
 * @date 2024-11-14 17:01
 */
@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
            key = {MqConst.ROUTING_ORDER}
    ))
    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
        //下单成功更新预约数
        Schedule schedule = scheduleService.getScheduleId(orderMqVo.getScheduleId());
        schedule.setReservedNumber(orderMqVo.getReservedNumber());
        schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        scheduleService.update(schedule);
        //发送短信
        SmsVo smsVo = orderMqVo.getSmsVo();
        if (null != smsVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_SMS, MqConst.ROUTING_SMS_ITEM, smsVo);
        }
    }

}
