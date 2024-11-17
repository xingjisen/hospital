package com.yygh.sms.service;

import com.yygh.vo.sms.SmsVo;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.sms.controller
 * @Description
 * @date 2024-10-20 21:32
 */
public interface SmsService {
    Map<String, Object> send(String phone);


    /**
     * MQ发送短信接口
     */
    boolean send(SmsVo smsVo);
}
