package com.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yygh.common.result.Result;
import com.yygh.model.user.UserInfo;
import com.yygh.user.service.UserInfoService;
import com.yygh.vo.user.UserInfoQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.controller
 * @Description
 * @date 2024-11-03 14:23
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @Operation(summary = "用户列表")
    @GetMapping
    public Result list(UserInfoQueryVo userInfoQueryVo) {
        IPage<UserInfo> list = userInfoService.list(userInfoQueryVo);
        return Result.success(list);
    }

    @Operation(summary = "用户锁定")
    @PutMapping("/lock/{id}/{status}")
    public Result lock(@PathVariable Long id, @PathVariable Integer status) {
        userInfoService.lock(id, status);
        return Result.success();
    }

    @Operation(summary = "详情信息")
    @PutMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        Map<String, Object> map = userInfoService.detail(id);
        return Result.success(map);
    }

    @Operation(summary = "认证审批")
    @PutMapping("/approval/{id}/{authStatus}")
    public Result approval(@PathVariable Long id, @PathVariable Integer authStatus) {
        userInfoService.approval(id, authStatus);
        return Result.success();
    }
}
