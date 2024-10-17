package com.yygh.user;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user
 * @Description
 * @date 2024-10-16 20:38
 */
@SpringBootApplication
@MapperScan("com.yygh.user.mapper")
@ComponentScan("com.yygh")
/**启动Nacos*/
@EnableDiscoveryClient
@RestController
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());
    }

    @GetMapping("/aaaa")
    public SaResult aaaa() {
        // 第1步，先登录上
        StpUtil.login(10001);
        // 第2步，获取 Token  相关参数
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 第3步，返回给前端
        return SaResult.data(tokenInfo);
    }
}
