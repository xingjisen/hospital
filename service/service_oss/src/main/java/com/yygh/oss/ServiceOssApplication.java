package com.yygh.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp
 * @Description
 * @date 2024-09-02 20:43
 */
@SpringBootApplication
//@MapperScan("com.yygh.oss.mapper")
@ComponentScan("com.yygh")
/**启动Nacos*/
@EnableDiscoveryClient
public class ServiceOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class, args);
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
