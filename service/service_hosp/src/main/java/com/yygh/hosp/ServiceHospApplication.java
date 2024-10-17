package com.yygh.hosp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp
 * @Description
 * @date 2024-09-02 20:43
 */
@SpringBootApplication
@MapperScan("com.yygh.hosp.mapper")
@ComponentScan("com.yygh")
/**启动Nacos*/
@EnableDiscoveryClient
/* 开启Feign*/
@EnableFeignClients(basePackages = "com.yygh")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);

    }
//    mybatis-plus3 mybatis2 需要加这个
//    @Bean
//    public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List ddlList) {
//        return new DdlApplicationRunner(ddlList);
//    }

}
