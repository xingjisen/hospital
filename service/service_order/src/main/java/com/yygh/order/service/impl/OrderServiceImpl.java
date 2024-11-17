package com.yygh.order.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.rabbit.constant.MqConst;
import com.yygh.common.rabbit.service.RabbitService;
import com.yygh.common.result.Result;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.common.utils.helper.HttpRequestHelper;
import com.yygh.enums.OrderStatusEnum;
import com.yygh.hosp.client.HospFeignClient;
import com.yygh.model.order.OrderInfo;
import com.yygh.model.user.Patient;
import com.yygh.order.mapper.OrderMapper;
import com.yygh.order.service.OrderService;
import com.yygh.user.client.PatientFeignClient;
import com.yygh.vo.hosp.ScheduleOrderVo;
import com.yygh.vo.order.OrderMqVo;
import com.yygh.vo.order.OrderQueryVo;
import com.yygh.vo.order.SignInfoVo;
import com.yygh.vo.sms.SmsVo;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    private PatientFeignClient patientFeignClient;
    private HospFeignClient hospFeignClient;
    private RabbitService rabbitService;


    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        // 就诊人信息
        Result res = patientFeignClient.getParentId(patientId);
        if (res.getCode() != 200) {
            throw new YyghException(ResultCodeEnum.CUSTOM);
        }
        Patient patient = Convert.convert(Patient.class, res.getData());
        //排班信息
        ScheduleOrderVo sheduleOrderVo = hospFeignClient.getSheduleOrderVo(scheduleId);
        // 判断是否可以预约
        Date startTime = sheduleOrderVo.getStartTime();
        Date endTime = sheduleOrderVo.getEndTime();
        if (!DateUtil.isIn(new Date(), startTime, endTime)) {
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

            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(scheduleId);
            orderMqVo.setReservedNumber(reservedNumber);
            orderMqVo.setAvailableNumber(availableNumber);

            SmsVo smsVo = new SmsVo();
            smsVo.setPhone(patient.getPhone());
//            smsVo.getParam().put("code", number);
//            smsVo.setTemplateCode("1818190");
            smsVo.setTemplateCode("2309269");
            String reserveDate = DateUtil.formatDate(orderInfo.getReserveDate()) + (orderInfo.getReserveTime() == 0 ? "上午" : "下午");

            OrderInfo finalOrderInfo = orderInfo;
            Map<String, Object> param = new HashMap<>() {{
                put("title", finalOrderInfo.getHoscode() + "|" + finalOrderInfo.getDepname() + "|" + finalOrderInfo.getTitle());
                put("amount", finalOrderInfo.getAmount());
                put("reserveDate", reserveDate);
                put("name", finalOrderInfo.getPatientName());
                put("quitTime", DateUtil.formatDate(finalOrderInfo.getQuitTime()));
            }};
            smsVo.setParam(param);
            orderMqVo.setSmsVo(smsVo);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);
        } else {
            throw new YyghException(result.getString("message"), ResultCodeEnum.FAIL.getCode());
        }
        return orderInfo.getId();

    }

    @Override
    public OrderInfo getOrders(String orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        return packOrderInfo(orderInfo);
    }

    @Override
    public IPage<OrderInfo> list(OrderQueryVo orderQueryVo) {
        IPage<OrderInfo> iPage = new Page(orderQueryVo.getPageNum(), orderQueryVo.getPageSize());
        IPage<OrderInfo> list = baseMapper.list(iPage, orderQueryVo);
        list.getRecords().forEach(this::packOrderInfo);
        return list;
    }


    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }
}
