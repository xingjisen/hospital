package com.yygh.oss.controller;

import com.yygh.common.result.Result;
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
}
