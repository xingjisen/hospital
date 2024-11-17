package com.yygh.user.controller;

import com.yygh.common.result.Result;
import com.yygh.model.user.Auth;
import com.yygh.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.controller
 * @Description
 * @date 2024-11-17 17:40
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/admin/user/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result list(@RequestBody Auth auth) {
        Map<String, Object> login = authService.login(auth);
        return Result.success(login);
    }

    @Operation(summary = "用户信息")
    @GetMapping("/{id}")
    public Result list(@PathVariable("id") String id) {
        Auth user = authService.detail(id);
        return Result.success(user);
    }
}
