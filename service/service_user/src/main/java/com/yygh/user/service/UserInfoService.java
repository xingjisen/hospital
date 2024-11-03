package com.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.user.UserInfo;
import com.yygh.vo.user.LoginVo;
import com.yygh.vo.user.UserAuthVo;
import com.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service
 * @Description
 * @date 2024-10-16 20:47
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户手机号登陆
     *
     * @param loginVo
     * @return
     */
    Map<String, Object> login(LoginVo loginVo);

    UserInfo selectWxInfoOpenId(String openid);

    void userAuth(UserAuthVo userAuthVo);

    UserInfo getUserInfo();

    IPage<UserInfo> list(UserInfoQueryVo userInfoQueryVo);

    void lock(Long id, Integer status);

    Map<String, Object> detail(Long id);

    void approval(Long id, Integer authStatus);
}
