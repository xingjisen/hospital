package com.yygh.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xingjisen
 * @author Jason
 * @PACKAGE_NAME com.yygh.user
 * @Description
 * @date 2024-10-16 20:38
 */
// 启动类，不自动加载数据库配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@MapperScan("com.yygh.sms.mapper")
@ComponentScan("com.yygh")
/**启动Nacos*/
@EnableDiscoveryClient
public class ServiceSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}
