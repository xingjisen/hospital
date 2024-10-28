package com.yygh.hosp.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSONObject;
import com.yygh.hosp.repository.ScheduleRepository;
import com.yygh.hosp.service.DepartService;
import com.yygh.hosp.service.HospitalService;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.hosp.BookingScheduleRuleVo;
import com.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartService departService;

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

    @Override
    public Map<String, Object> getScheduleRule(ScheduleQueryVo scheduleQueryVo) {

        // 根据医院编号和科室编号查询数据
        Criteria criteria = Criteria.where("hoscode").is(scheduleQueryVo.getHoscode()).and("depcode").is(scheduleQueryVo.getDepcode());

        //根据工作日 workDate 进行分组 做聚合操作
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配条件
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        // 统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                // 排序
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                // 分页
                Aggregation.skip((scheduleQueryVo.getPageNum() - 1) * scheduleQueryVo.getPageSize()),
                Aggregation.limit(scheduleQueryVo.getPageSize())
        );
        AggregationResults<BookingScheduleRuleVo> aggResults = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResults.getMappedResults();

        // 分组查询总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        int total = mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class).getMappedResults().size();

        // 获取日期对应星期
        bookingScheduleRuleVoList.forEach(bookingScheduleRuleVo -> {
            bookingScheduleRuleVo.setDayOfWeek(getWeek(bookingScheduleRuleVo.getWorkDate()));
        });
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleVoList", bookingScheduleRuleVoList);
        result.put("total", total);
        // 获取医院名称
        String hosName = hospitalService.getHosName(scheduleQueryVo.getHoscode());
        // 其他数据
        Map<String, String> base = new HashMap<>();
        base.put("hosname", hosName);
        result.put("baseMap", base);
        return result;


    }

    @Override
    public List<Schedule> getDetailSchedule(ScheduleQueryVo scheduleQueryVo) {
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(scheduleQueryVo.getHoscode(), scheduleQueryVo.getDepcode(), scheduleQueryVo.getWorkDate());
        scheduleList.forEach(schedule -> packageSchedule(schedule));


        return scheduleList;
    }

    private void packageSchedule(Schedule schedule) {
        // 设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHosName(schedule.getHoscode()));
        schedule.getParam().put("depname", departService.getDepartName(schedule.getHoscode(), schedule.getDepcode()));
        schedule.getParam().put("dayOfWeek", getWeek(schedule.getWorkDate()));

    }

    private String getWeek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }
}
