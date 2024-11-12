package com.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.hosp.ScheduleOrderVo;
import com.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service
 * @Description
 * @date 2024-09-21 15:42
 */
public interface ScheduleService extends IService<Schedule> {
    void save(Map<String, Object> paramMap);

    Page<Schedule> list(ScheduleQueryVo scheduleQueryVo);

    void remove(Schedule schedule);

    Map<String, Object> getScheduleRule(ScheduleQueryVo scheduleQueryVo);

    List<Schedule> getDetailSchedule(ScheduleQueryVo scheduleQueryVo);

    Map<String, Object> bookingScheduleRule(ScheduleQueryVo scheduleQueryVo);

    Schedule getSheduleId(String scheduleId);

    ScheduleOrderVo getSheduleOrderVo(String scheduleId);

}
