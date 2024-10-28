package com.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.oss.service
 * @Description
 * @date 2024-10-28 21:44
 */
public interface OssService {
    String upload(MultipartFile multipartFile);
}
