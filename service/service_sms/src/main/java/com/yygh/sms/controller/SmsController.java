package com.yygh.sms.controller;

import com.yygh.common.result.Result;
import com.yygh.sms.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.sms.controller
 * @Description
 * @date 2024-10-20 21:31
 */
@Tag(name = "短信管理")
@RestController
@RequestMapping("/api/sms")
public class SmsController {
    @Autowired
    private SmsService smsService;


    @Operation(summary = "发送手机验证码")
    @GetMapping("/send/{phone}")
    public Result sendCode(@PathVariable String phone) {
        Map<String, Object> sendObj = smsService.send(phone);
        if (!(boolean) sendObj.get("isCode")) {
            return Result.fail(sendObj.get("msg"));
        }
        return Result.success(sendObj.get("code"));
    }

}
