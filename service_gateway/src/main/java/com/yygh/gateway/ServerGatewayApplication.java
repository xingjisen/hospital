package com.yygh.gateway;

/*
  @author Jason
 * @PACKAGE_NAME com.yygh.gateway
 * @Description
 * @date 2024-09-28 18:01
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RefreshScope // 动态刷新配置
public class ServerGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }


    @Value("${share.config1}")
    private String shareConfig1;

    @Value("${share.config2}")
    private String shareConfig2;


    @GetMapping("/getShare1")
    public String getShare1() {
        return shareConfig1;
    }

    @GetMapping("/getShare2")
    public String getShare2() {
        return shareConfig2;
    }


}
