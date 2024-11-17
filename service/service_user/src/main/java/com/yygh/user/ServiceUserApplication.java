package com.yygh.user;

import cn.dev33.satoken.SaManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user
 * @Description
 * @date 2024-10-16 20:38
 */
@SpringBootApplication
@MapperScan("com.yygh.user.mapper")
@ComponentScan("com.yygh")
@EnableFeignClients("com.yygh.cmn")
/**启动Nacos*/
@EnableDiscoveryClient
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
        System.out.println("启动成功,Sa-Token 配置如下：" + SaManager.getConfig());
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
