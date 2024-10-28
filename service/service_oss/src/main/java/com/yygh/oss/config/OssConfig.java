package com.yygh.oss.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.oss
 * @Description
 * @date 2024-10-26 14:14
 */
@Configuration
@ConfigurationProperties("tencent.oss")
@Data
public class OssConfig implements InitializingBean {
    private String secretId;
    private String secretKey;
    private String bucketName;
    private String regionName;

    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String BUCKET_NAME;
    public static String REGION_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        BUCKET_NAME = bucketName;
        REGION_NAME = regionName;
    }
}
