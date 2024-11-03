package com.yygh.user.api;

import com.yygh.common.result.Result;
import com.yygh.model.user.UserInfo;
import com.yygh.user.service.UserInfoService;
import com.yygh.vo.user.LoginVo;
import com.yygh.vo.user.UserAuthVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.controller
 * @Description
 * @date 2024-10-16 20:41
 */
@Tag(name = "用户登录管理")
@RestController
@RequestMapping("/api/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;


    @Operation(summary = "用户手机号登陆")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> login = userInfoService.login(loginVo);
        return Result.success(login);
    }

    @Operation(summary = "完善用户信息")
    @PostMapping("/auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo) {
        userInfoService.userAuth(userAuthVo);
        return Result.success();
    }

    @Operation(summary = "根据ID获取用户信息")
    @GetMapping("/auth/getUserInfo")
    public Result getUserInfo() {
        UserInfo userInfo = userInfoService.getUserInfo();
        return Result.success(userInfo);
    }
}
