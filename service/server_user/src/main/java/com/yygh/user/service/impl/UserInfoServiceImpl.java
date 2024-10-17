package com.yygh.user.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.model.user.UserInfo;
import com.yygh.user.mapper.UserInfoMapper;
import com.yygh.user.service.UserInfoService;
import com.yygh.vo.user.LoginVo;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service.impl
 * @Description
 * @date 2024-10-16 20:48
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        if (phone.isEmpty() || code.isEmpty()) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (userInfo == null) {
            // 第一次登陆
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setStatus(1);
            int insert = baseMapper.insert(userInfo);
        }
        if (userInfo.getStatus().equals(0)) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        Map<String, Object> map = new HashMap<>();

        String name = userInfo.getName();
        if (StringUtil.isNullOrEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtil.isNullOrEmpty(name)) {
            name = userInfo.getPhone();
        }
        StpUtil.login(userInfo.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        map.put("name", name);
        map.put("token", tokenInfo.getTokenValue());
        return map;
    }
}
