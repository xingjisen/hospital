package com.yygh.sms.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.sms.config
 * @Description
 * @date 2024-10-20 21:16
 */
@Configuration
@ConfigurationProperties("tencent.sms")
@Data
public class SmsConfig implements InitializingBean {
    private List<String> phoneNumberSet;
    private String smsSdkAppId;
    private String templateId;
    private String signName;
    private String secretId;
    private String secretKey;
    private String codeTime;

    public static List<String> PHONE_NUMBER_SET;
    public static String SMS_SDK_APP_ID;
    public static String TEMPLATE_ID;
    public static String SIGN_NAME;
    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String CODE_TIME;

    @Override
    public void afterPropertiesSet() throws Exception {
        PHONE_NUMBER_SET = phoneNumberSet;
        SMS_SDK_APP_ID = smsSdkAppId;
        TEMPLATE_ID = templateId;
        SIGN_NAME = signName;
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        CODE_TIME = codeTime;
    }

}
