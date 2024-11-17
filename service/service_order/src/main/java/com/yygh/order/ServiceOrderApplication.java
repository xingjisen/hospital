package com.yygh.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.order
 * @Description
 * @date 2024-11-07 22:27
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.yygh"})
@MapperScan(basePackages = {"com.yygh.order.mapper"})
@EnableFeignClients(basePackages = {"com.yygh.hosp", "com.yygh.user"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
        System.out.println("""
                《═══════════════════════》 \s
                  ★✔️ 系统已完美启动 ★ ✔️   \s
                《═══════════════════════》
                   ✨ ★( •_•)>⌐■-■    ✨ ★
                 (⌐■_■) ✧炫酷登场  ✧٩(ˊᗜˋ*)و✧\s
                ━━━━━━━━━━━✦✧✦━━━━━━━━━━━ \s
                   (ﾉ>ω<)ﾉ ✩✩✩ BOOM! \s
                """);
    }
}
