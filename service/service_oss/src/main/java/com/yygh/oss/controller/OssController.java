package com.yygh.oss.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.yygh.common.result.Result;
import com.yygh.common.utils.AuthContextUtil;
import com.yygh.oss.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.oss.controller
 * @Description
 * @date 2024-10-28 21:43
 */
@Tag(name = "文件")
@RestController
@RequestMapping("/api/oss/file")
public class OssController {

    @Autowired
    private OssService ossService;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public Result upload(MultipartFile multipartFile) {
        String url = ossService.upload(multipartFile);
        return Result.success(url);
    }

    @Operation(summary = "文件上传")
    @PostMapping("/aaa")
    public Result aaa(MultipartFile multipartFile) {
        // 获取当前会话是否已经登录，返回true=已登录，false=未登录
        boolean login = StpUtil.isLogin();
        System.out.println(login);
        System.out.println(AuthContextUtil.getUserId());
        System.out.println(AuthContextUtil.getUserName());
        return Result.success("url");
    }
}
