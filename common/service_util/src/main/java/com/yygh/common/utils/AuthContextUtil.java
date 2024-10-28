package com.yygh.common.utils;

import cn.dev33.satoken.stp.StpUtil;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.common.utils
 * @Description 获取用户信息
 * @date 2024-10-28 22:19
 */
public class AuthContextUtil {
    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        String loginId = (String) StpUtil.getLoginId();
        return Long.parseLong(loginId.split(Separator.TOKEN_SEPARATOR)[0]);
    }

    /**
     * 获取用过户名称
     */
    public static String getUserName() {
        String loginId = (String) StpUtil.getLoginId();
        return loginId.split(Separator.TOKEN_SEPARATOR)[1];
    }
}
