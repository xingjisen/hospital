package com.yygh.hosp.client;

import com.yygh.vo.hosp.ScheduleOrderVo;
import com.yygh.vo.order.SignInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.client
 * @Description
 * @date 2024-11-12 22:00
 */
@FeignClient(value = "service-hosp")
@Repository
public interface HospFeignClient {
    /**
     * 根据排班ID获取预约下单数据
     *
     * @param scheduleId
     * @return
     */
    @GetMapping("/api/hosp/inner/getSheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getSheduleOrderVo(@PathVariable("scheduleId") String scheduleId);


    /**
     * 根据排医院编号获取签名信息
     *
     * @param hoscode
     * @return
     */
    @GetMapping("/inner/getSiggnInfoVo/{hoscode}")
    public SignInfoVo getSiggnInfoVo(@PathVariable("hoscode") String hoscode);
}
