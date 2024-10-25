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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service.impl
 * @Description
 * @date 2024-10-16 20:48
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        if (phone.isEmpty() || code.isEmpty()) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 判断验证码
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        // 判断是微信登陆还是手机号登陆
        UserInfo userInfo = null;
        if (!StringUtil.isNullOrEmpty(loginVo.getOpenid())) {
            // 微信登陆
            // 查询数据库是否有这条数据
            userInfo = selectWxInfoOpenId(loginVo.getOpenid());
            if (userInfo != null) {
                userInfo.setPhone(loginVo.getPhone());
                // 更新数据
                updateById(userInfo);
                // TODO 两种选择：1.合并用户数据,2.删除手机号数据 保留微信。选用第二种
                QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("phone", loginVo.getPhone());
                List<UserInfo> userIndos = baseMapper.selectList(wrapper);
                userIndos.forEach(user -> {
                    if (user.getOpenid() == null) {
                        baseMapper.deleteById(user.getId());
                    }
                });
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

        if (userInfo == null) {
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo == null) {
                // 第一次登陆
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(loginVo.getPhone());
                userInfo.setStatus(1);
                baseMapper.insert(userInfo);
            }
            if (userInfo.getStatus().equals(0)) {
                throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
            }
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

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return baseMapper.selectOne(queryWrapper);
    }
}
