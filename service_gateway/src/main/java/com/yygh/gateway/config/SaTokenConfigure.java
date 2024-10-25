package com.yygh.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.hutool.json.JSONUtil;
import com.yygh.common.result.Result;
import com.yygh.common.result.ResultCodeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 全局配置类
 *
 * @author xingjisen
 */
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 指定 [拦截路由] 拦截inner
                .addInclude("/*/inner/**")
                // 指定 [放行路由]
                .addExclude("/api/hosp/**", "/admin/cmn/dict/**")
                // 指定[认证函数]: 每次请求执行
                .setAuth(obj -> {
                    System.out.println("---------- 进入Sa-Token全局认证 -----------" + obj);
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                // 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
                .setError(e -> {
                    // 设置响应头
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    System.out.println("---------- sa全局异常 " + e.getMessage());
                    // 使用封装的 JSON 工具类转换数据格式
                    return JSONUtil.toJsonStr(Result.fail(ResultCodeEnum.LOGIN_AUTH.getCode(), e.getMessage()));
                });
    }
}
