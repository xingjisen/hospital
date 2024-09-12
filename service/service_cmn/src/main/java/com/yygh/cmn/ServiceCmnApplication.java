package com.yygh.cmn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp
 * @Description
 * @date 2024-09-02 20:43
 */
@SpringBootApplication
@MapperScan("com.yygh.cmn.mapper")
@ComponentScan("com.yygh")
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class, args);
    }
//    mybatis-plus3 mybatis2 需要加这个
//    @Bean
//    public DdlApplicationRunner ddlApplicationRunner(@Autowired(required = false) List ddlList) {
//        return new DdlApplicationRunner(ddlList);
//    }

}