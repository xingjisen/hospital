package com.yygh.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.model.user.Auth;
import com.yygh.user.mapper.AuthMapper;
import com.yygh.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service.impl
 * @Description
 * @date 2024-11-17 17:43
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Override
    public Map<String, Object> login(Auth auth) {
        Map<String, Object> map = new HashMap<>();
        if (auth.getUserName() != null || auth.getPhone() != null) {
            if (auth.getPassword() == null) {
                throw new YyghException("未输入密码", ResultCodeEnum.CUSTOM.getCode());
            }
            Auth user = authMapper.getUser(auth);
            if (user == null) {
                throw new YyghException("用户不存在", ResultCodeEnum.CUSTOM.getCode());
            }
            if (!user.getPassword().equals(auth.getPassword())) {
                throw new YyghException("密码输入错误", ResultCodeEnum.CUSTOM.getCode());
            }
            StpUtil.login(user.getId());
            map.put("token", StpUtil.getTokenValue());
            map.put("nickName", user.getNickName());
            map.put("id", user.getId());
        } else {
            throw new YyghException("未输入用户名", ResultCodeEnum.CUSTOM.getCode());
        }
        return map;
    }

    @Override
    public Auth detail(String id) {
        Auth auth = authMapper.selectById(id);
        return auth;
    }
}
