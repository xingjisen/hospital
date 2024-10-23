package com.yygh.user.service;

import com.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service
 * @Description
 * @date 2024-10-16 20:47
 */
public interface UserInfoService {

    /**
     * 用户手机号登陆
     *
     * @param loginVo
     * @return
     */
    Map<String, Object> login(LoginVo loginVo);
}
