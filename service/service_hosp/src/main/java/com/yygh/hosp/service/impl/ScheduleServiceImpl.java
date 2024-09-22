package com.yygh.hosp.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSONObject;
import com.yygh.hosp.repository.ScheduleRepository;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service.impl
 * @Description
 * @date 2024-09-21 15:43
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(jsonString, Schedule.class);
        Schedule bySchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (bySchedule != null) {
            schedule.setId(bySchedule.getId());
            schedule.setCreateTime(bySchedule.getCreateTime());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(bySchedule.getIsDeleted());
            schedule.setStatus(1);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
        }
        scheduleRepository.save(schedule);
    }

    @Override
    public Page<Schedule> list(ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable = PageRequest.of(scheduleQueryVo.getPageNum(), scheduleQueryVo.getPageSize());
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Schedule schedule = Convert.convert(Schedule.class, scheduleQueryVo);
        Example<Schedule> example = Example.of(schedule, matcher);
        return scheduleRepository.findAll(example, pageable);
    }

    @Override
    public void remove(Schedule schedule) {
        Schedule scheduleByHoscodeAndHosScheduleId = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (scheduleByHoscodeAndHosScheduleId != null) {
            scheduleRepository.deleteById(scheduleByHoscodeAndHosScheduleId.getId());
        }
    }
}
