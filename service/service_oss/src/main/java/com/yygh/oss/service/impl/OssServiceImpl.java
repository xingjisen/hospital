package com.yygh.oss.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.oss.config.OssConfig;
import com.yygh.oss.mapper.OssMapper;
import com.yygh.oss.service.OssService;
import com.yygh.oss.utils.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.oss.service.impl
 * @Description
 * @date 2024-10-28 21:45
 */
@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OssMapper ossMapper;

    @Override
    public String upload(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();
        // 时间
        String date = LocalDateTimeUtil.format(LocalDate.now(), "YYYY/MM/dd");
        // 获取文件名
        String fileName = date + "/" + uuid + multipartFile.getOriginalFilename();

        // TODO 后期根据上传人的标识增加一个文件夹
        // 获取文件后缀
        String prefix = null;
        try {
            prefix = fileName.substring(fileName.lastIndexOf("."));
        } catch (Exception e) {
            throw new YyghException("文件后缀获取失败", ResultCodeEnum.CUSTOM.getCode());
        }
/*        // 方式1
        Path tempFile = null;
        tempFile = Files.createTempFile(fileName.substring(0, fileName.lastIndexOf(".")), prefix);

        multipartFile.transferTo(tempFile);
        File file = tempFile.toFile();
        OssUtil.put(fileName, file);*/

        // 方式2
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);
            OssUtil.put(fileName, file);
            //返回url
            return "https://" + OssConfig.BUCKET_NAME + ".cos." + OssConfig.REGION_NAME + ".myqcloud.com/" + fileName;
        } catch (Exception e) {
            throw new YyghException("文件上传失败" + e.getMessage(), ResultCodeEnum.CUSTOM.getCode());
        }
    }
}
