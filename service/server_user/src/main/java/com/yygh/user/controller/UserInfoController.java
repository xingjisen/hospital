package com.yygh.user.controller;

import com.yygh.common.result.Result;
import com.yygh.user.service.UserInfoService;
import com.yygh.vo.user.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.controller
 * @Description
 * @date 2024-10-16 20:41
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;


    @Operation(summary = "用户手机号登陆")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> login = userInfoService.login(loginVo);
        return Result.success(login);
    }
}
