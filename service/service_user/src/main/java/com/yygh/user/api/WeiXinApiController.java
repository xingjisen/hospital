package com.yygh.user.api;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.yygh.common.result.Result;
import com.yygh.common.utils.OkHttp3Util;
import com.yygh.common.utils.Separator;
import com.yygh.model.user.UserInfo;
import com.yygh.user.service.UserInfoService;
import com.yygh.user.utils.ConstantPropertiesUtil;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.api
 * @Description
 * @date 2024-10-24 19:57
 */
@Controller
@Tag(name = "微信登陆")
@RequestMapping("/api/ucenter/wx")
public class WeiXinApiController {
    @Autowired
    private UserInfoService userInfoService;


    // 生成微信扫描二维码
    @GetMapping("/getLoginParam")
    @ResponseBody
    @Operation(summary = "返回微信二维码所需参数")
    public Result getLoginParam() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
            map.put("scope", "snsapi_login");
            String wxOpenRedirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
            URLEncoder.encode(wxOpenRedirectUrl, "utf-8");
            map.put("redirect_uri", wxOpenRedirectUrl);
            map.put("state", System.currentTimeMillis());
            return Result.success(map);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/callback")
//    @ResponseBody
    @Operation(summary = "扫码后的回调方法")
    public String callback(String code, String state) throws IOException {
        System.out.println("临时票据: " + code);
        System.out.println(state);

        Map<String, String> param = new HashMap<>();
        param.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
        param.put("secret", ConstantPropertiesUtil.WX_OPEN_APP_SECRET);
        param.put("code", code);
        param.put("grant_type", "authorization_code");

        String s = OkHttp3Util.get("https://api.weixin.qq.com/sns/oauth2/access_token", param);
        System.out.println(s);
        JSONObject userobj = JSONObject.parse(s);
        String openid = (String) userobj.get("openid");
        String access_token = (String) userobj.get("access_token");
        UserInfo userInfo = userInfoService.selectWxInfoOpenId(openid);
        if (userInfo == null) {
            // 通过access_token和openid获取用户信息
            Map<String, String> userParam = new HashMap<>();
            userParam.put("access_token", access_token);
            userParam.put("openid", openid);
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
            String userInfoStr = OkHttp3Util.get(userInfoUrl, userParam);

            // 解析用户信息
            JSONObject userInfoObj = JSONObject.parse(userInfoStr);
            String name = (String) userInfoObj.get("nickname");
            String headPortrait = (String) userInfoObj.get("headimgurl");


            userInfo = new UserInfo();
            userInfo.setNickName(name);
            userInfo.setOpenid(openid);
            userInfo.setStatus(1);
            userInfoService.save(userInfo);
        }

        //返回用户信息和token

        String name = userInfo.getName();
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNullOrEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtil.isNullOrEmpty(name)) {
            name = userInfo.getPhone();
        }
        map.put("name", name);

        if (StringUtil.isNullOrEmpty(userInfo.getPhone())) {
            map.put("openid", userInfo.getOpenid());
        } else {
            map.put("openid", "");
        }

        StpUtil.login(userInfo.getId() + Separator.TOKEN_SEPARATOR + name);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        map.put("name", name);
        map.put("token", tokenInfo.getTokenValue());
//        return map;
//        boolean isPhone = !StringUtil.isNullOrEmpty(userInfo.getPhone());
//        return "登陆成功,登录信息为: " + name;
        return "redirect:" +
                ConstantPropertiesUtil.YYGH_BASE_URL +
                "/weixin/callback?token=" + map.get("token") +
                "&openid=" + map.get("openid") +
                "&name=" + URLEncoder.encode((String) map.get("name"), StandardCharsets.UTF_8);
//                "&phone=" + (isPhone ? userInfo.getPhone() : "")
    }

    // 得到扫码人信息
}
