package com.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.user.Auth;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service
 * @Description
 * @date 2024-10-16 20:47
 */
public interface AuthService extends IService<Auth> {
    Map<String, Object> login(Auth auth);

    Auth detail(String id);
}
