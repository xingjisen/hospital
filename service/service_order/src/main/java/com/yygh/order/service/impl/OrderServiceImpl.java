package com.yygh.order.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.common.utils.helper.HttpRequestHelper;
import com.yygh.enums.OrderStatusEnum;
import com.yygh.hosp.client.HospFeignClient;
import com.yygh.hosp.client.PatientFeignClient;
import com.yygh.model.order.OrderInfo;
import com.yygh.model.user.Patient;
import com.yygh.order.mapper.OrderMapper;
import com.yygh.order.service.OrderService;
import com.yygh.vo.hosp.ScheduleOrderVo;
import com.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.order.service.impl
 * @Description
 * @date 2024-11-07 22:35
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    private PatientFeignClient patientFeignClient;
    private HospFeignClient hospFeignClient;


    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        // 就诊人信息
        Patient patient = patientFeignClient.getParentId(patientId);
        //排班信息
        ScheduleOrderVo sheduleOrderVo = hospFeignClient.getSheduleOrderVo(scheduleId);
        // 判断是否可以预约
        Date startTime = sheduleOrderVo.getStartTime();
        Date endTime = sheduleOrderVo.getEndTime();
        if (!DateUtil.isIn(startTime, new Date(), endTime)) {
            throw new YyghException(ResultCodeEnum.TIME_NO);
        }

        // 获取签名信息
        SignInfoVo signInfoVo = hospFeignClient.getSiggnInfoVo(sheduleOrderVo.getHoscode());
        // 添加到订单表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo = Convert.convert(OrderInfo.class, sheduleOrderVo);

        String outTradeNo = System.currentTimeMillis() + "" + new SecureRandom().nextInt(100);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setScheduleId(scheduleId);
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(patientId);
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());
//        save(orderInfo);
        baseMapper.insert(orderInfo);

        // 调用该医院接口实现预约挂号操作
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode", orderInfo.getHoscode());
        paramMap.put("depcode", orderInfo.getDepcode());
        paramMap.put("hosScheduleId", orderInfo.getScheduleId());
        paramMap.put("reserveDate", new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", orderInfo.getReserveTime());
        paramMap.put("amount", orderInfo.getAmount());
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType", patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex", patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("phone", patient.getPhone());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode", patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode", patient.getDistrictCode());
        paramMap.put("address", patient.getAddress());
        //联系人
        paramMap.put("contactsName", patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo", patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone", patient.getContactsPhone());
        paramMap.put("timestamp", System.currentTimeMillis());
        String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign", sign);
        JSONObject result = HttpRequestHelper.sendRequest(paramMap, signInfoVo.getApiUrl() + "/order/submitOrder");

        if (result.getInteger("code") == 200) {
            JSONObject jsonObject = result.getJSONObject("data");
            //预约记录唯一标识（医院预约记录主键）
            String hosRecordId = jsonObject.getString("hosRecordId");
            //预约序号
            Integer number = jsonObject.getInteger("number");

            //取号时间
            String fetchTime = jsonObject.getString("fetchTime");

            //取号地址
            String fetchAddress = jsonObject.getString("fetchAddress");

            //更新订单
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            baseMapper.updateById(orderInfo);
            //排班可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            //发送mq信息更新号源和短信通知
        } else {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
        return orderInfo.getId();

    }
}
