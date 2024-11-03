package com.yygh.user.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.common.utils.AuthContextUtil;
import com.yygh.common.utils.Separator;
import com.yygh.enums.AuthStatusEnum;
import com.yygh.model.user.Patient;
import com.yygh.model.user.UserInfo;
import com.yygh.user.mapper.UserInfoMapper;
import com.yygh.user.service.PatientService;
import com.yygh.user.service.UserInfoService;
import com.yygh.vo.user.LoginVo;
import com.yygh.vo.user.UserAuthVo;
import com.yygh.vo.user.UserInfoQueryVo;
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

    @Autowired
    private PatientService patientService;

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
        StpUtil.login(userInfo.getId() + Separator.TOKEN_SEPARATOR + name);
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

    @Override
    public void userAuth(UserAuthVo userAuthVo) {
        Long userId = AuthContextUtil.getUserId();
        UserInfo userInfo = baseMapper.selectById(userId);

        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());

        baseMapper.updateById(userInfo);
    }

    @Override
    public UserInfo getUserInfo() {
        Long userId = AuthContextUtil.getUserId();
        return baseMapper.selectById(userId);
    }

    @Override
    public IPage<UserInfo> list(UserInfoQueryVo userInfoQueryVo) {
        if (userInfoQueryVo.getPageNum() == null && userInfoQueryVo.getPageSize() == null) {
            throw new YyghException("没有页码等数据", ResultCodeEnum.CUSTOM.getCode());
        }
        long pageNum = Long.parseLong(String.valueOf(userInfoQueryVo.getPageNum()));
        long pageSize = Long.parseLong(String.valueOf(userInfoQueryVo.getPageSize()));
        Page<UserInfo> page = new Page<>(pageNum, pageSize);
        IPage<UserInfo> list = baseMapper.list(page, userInfoQueryVo);
        list.getRecords().forEach(userInfo -> packageUserInfo(userInfo));
        return list;
    }

    @Override
    public void lock(Long id, Integer status) {
        if (status == 0 || status == 1) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> detail(Long id) {
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = packageUserInfo(baseMapper.selectById(id));
        map.put("userInfo", userInfo);
        // 就诊人信息
        List<Patient> patientList = patientService.listById(id);
        map.put("patientList", patientList);
        return map;
    }

    @Override
    public void approval(Long id, Integer authStatus) {
        if (authStatus == 2 || authStatus == -1) {
            UserInfo userInfo = baseMapper.selectById(id);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    /**
     * 编号对应值封装
     */
    private UserInfo packageUserInfo(UserInfo userInfo) {
        // 认证状态编码处理
        userInfo.getParam().put("authStatusName", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //用户状态处理
        userInfo.getParam().put("statusName", userInfo.getStatus().intValue() == 1 ? "正常" : "锁定");

        return userInfo;
    }
}
