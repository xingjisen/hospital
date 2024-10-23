package com.yygh.sms.service;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.yygh.common.utils.RdmUtil;
import com.yygh.sms.config.SmsConfig;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.sms.service
 * @Description
 * @date 2024-10-20 21:32
 */
@Service
@AllArgsConstructor // 简化构造器注入过程，通过构造注入
public class SmsServiceImpl implements SmsService {
    //    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Map<String, Object> send(String phone) {
        Map<String, Object> map = new HashMap<>();
        String phoneContent = "^1[3,4,5,6,7,8,9][0-9]{9}$";
        boolean isMatch = ReUtil.isMatch(phoneContent, phone);
        if (!isMatch) {
            map.put("code", "code");
            map.put("isCode", false);
            map.put("msg", "手机号验证不通过!");
            return map;
        }
        // redis获取验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StrUtil.hasEmpty(code)) {
            map.put("code", code);
            map.put("isCode", true);
            map.put("msg", "");
            return map;
        }
        //获取不到重新发送
        String newCode = RdmUtil.random6Code();
        // 短信发送出去
        Map<String, Object> resp = sendTencentCode(phone, newCode);
        if ("Ok".equals(resp.get("Code"))) {
            // 放到redis
            redisTemplate.opsForValue().set(phone, newCode, 10, TimeUnit.MINUTES);
            map.put("code", newCode);
            map.put("isCode", true);
            map.put("msg", "");
        } else {
            map.put("code", "");
            map.put("isCode", false);
            map.put("msg", resp.get("message"));
        }
        return map;
    }

    private Map<String, Object> sendTencentCode(String phone, String newCode) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(SmsConfig.SECRET_ID, SmsConfig.SECRET_KEY);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId(SmsConfig.SMS_SDK_APP_ID);
            req.setTemplateId(SmsConfig.TEMPLATE_ID);
            req.setSignName(SmsConfig.SIGN_NAME);

            String[] templateParamSet1 = {newCode, SmsConfig.CODE_TIME};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            map.put("Code", resp.getSendStatusSet()[0].getCode());
            map.put("message", resp.getSendStatusSet()[0].getMessage());
            map.put("phoneNumber", resp.getSendStatusSet()[0].getPhoneNumber());
            // 输出json格式的字符串回包
            System.out.println(AbstractModel.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            map.put("Code", "0");
            map.put("message", e.toString());
            map.put("phoneNumber", "");
        }
        return map;
    }
}
