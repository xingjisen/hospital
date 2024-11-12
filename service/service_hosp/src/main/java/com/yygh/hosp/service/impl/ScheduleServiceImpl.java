package com.yygh.hosp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.hosp.mapper.ScheduleMapper;
import com.yygh.hosp.repository.ScheduleRepository;
import com.yygh.hosp.service.DepartService;
import com.yygh.hosp.service.HospitalService;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.model.hosp.BookingRule;
import com.yygh.model.hosp.Department;
import com.yygh.model.hosp.Hospital;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.hosp.BookingScheduleRuleVo;
import com.yygh.vo.hosp.ScheduleOrderVo;
import com.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service.impl
 * @Description
 * @date 2024-09-21 15:43
 */
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {
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
        if (scheduleQueryVo.getHoscode() != null && scheduleQueryVo.getDepcode() != null && scheduleQueryVo.getWorkDate() != null) {
            List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(scheduleQueryVo.getHoscode(), scheduleQueryVo.getDepcode(), scheduleQueryVo.getWorkDate());
            scheduleList.forEach(schedule -> packageSchedule(schedule));
            return scheduleList;
        } else {
            throw new YyghException("数据校验不通过", ResultCodeEnum.CUSTOM.getCode());
        }
    }

    @Override
    public Map<String, Object> bookingScheduleRule(ScheduleQueryVo scheduleQueryVo) {
        Map<String, Object> result = new HashMap<>();
        // 获取预约规则
        Hospital hoscode = hospitalService.getByHoscode(scheduleQueryVo.getHoscode());
        BookingRule bookingRule;
        if (hoscode != null) {
            bookingRule = hoscode.getBookingRule();
        } else {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        //获取可预约日期数据
        IPage iPage = getListDate(scheduleQueryVo.getPageNum(), scheduleQueryVo.getPageSize(), bookingRule);
        List<Date> dateList = iPage.getRecords();

        // 获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(scheduleQueryVo.getHoscode()).and("depcode").is(scheduleQueryVo.getDepcode()).and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregateResult = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregateResult.getMappedResults();

        // 合并数据 map集合 key日期，value预约规则预约数量等
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = null;
        if (!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(Collectors.toMap(bookingScheduleRuleVo -> bookingScheduleRuleVo.getWorkDate(), bookingScheduleRuleVo -> bookingScheduleRuleVo));
        }
        // 获取可预约排班规则
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            Date date = dateList.get(i);
            // map集合获取key日期获取value
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);

            // 如果当天没有排班医生
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                // 就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
                // 科室剩余预约数 -1代表无号
                bookingScheduleRuleVo.setAvailableNumber(1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);

            // 计算当前预约日期对应星期
            String week = getWeek(new Date());
            bookingScheduleRuleVo.setDayOfWeek(week);

            // 最后一页最后一条记录为即将预约 状态 0正常 1即将放好 -1当天停止放号
            if (i == len - 1 && scheduleQueryVo.getPageSize() == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            // 当天过了预约时间不能预约
            if (i == 0 && scheduleQueryVo.getPageSize() == 1) {
                DateTime stopTime = getDateTime(new Date(), bookingRule.getStopTime());
                if (stopTime.isBefore(new Date())) {
                    // 停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        // 可预约日期规则数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hospitalService.getHosName(scheduleQueryVo.getHoscode()));

        Department department = departService.getDepartment(scheduleQueryVo.getHoscode(), scheduleQueryVo.getDepcode());
        baseMap.put("bigname", department.getBigname());
        baseMap.put("depname", department.getDepname());
        baseMap.put("workDateString", DateUtil.format(new Date(), "yyyy年MM月"));
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }

    @Override
    public Schedule getSheduleId(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        return packageSchedule(schedule);
    }

    @Override
    public ScheduleOrderVo getSheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        // 获取排班信息
        Schedule schedule = baseMapper.selectById(scheduleId);
        if (schedule == null) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        // 获取预约规则信息
        Hospital hospital = hospitalService.getByHoscode(schedule.getHoscode());
        if (hospital == null) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if (bookingRule == null) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        scheduleOrderVo.setHoscode(hospital.getHoscode());
        scheduleOrderVo.setHosname(hospitalService.getHosName(schedule.getHoscode()));
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departService.getDepartName(schedule.getHoscode(), schedule.getDepcode()));
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        // 退号截止天数
        int quintDay = bookingRule.getQuitDay();
        DateTime quitTime = getDateTime(DateUtil.offsetDay(new DateTime(schedule.getWorkDate()), quintDay), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toJdkDate());

        //预约开始时间
        DateTime startTime = getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toJdkDate());

        // 预约结束时间
        DateTime endTime = getDateTime(DateUtil.offsetDay(new DateTime(), bookingRule.getCycle()), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toJdkDate());

        // 当天停止挂号时间
        DateTime stopTime = getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toJdkDate());

        return scheduleOrderVo;
    }


    /**
     * 获取可预约日期分页数据
     */
    private IPage getListDate(Integer pageNum, Integer pageSize, BookingRule bookingRule) {
        // 当天放号信息 年月日时分
        DateTime releaseTime = getDateTime(new Date(), bookingRule.getReleaseTime());
        // 预约周期
        Integer cycle = bookingRule.getCycle();
        DateTime releaseTimeEnd = DateUtil.parse(bookingRule.getReleaseTime());
        if (releaseTime.isBefore(releaseTimeEnd)) {
            cycle += 1;
        }
        // 所有可预约日期，最后一天显示即将放号
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            DateTime dateTime = DateUtil.offsetDay(new Date(), i);
            String date = DateUtil.format(dateTime, "yyyy-MM-dd");
            dateList.add(new DateTime(date));
        }
        //分页，可有可无，前端修改为全部展示，超出展示滑块
        List<Date> pageDateList = new ArrayList<>();
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        if (end > dateList.size()) {
            end = dateList.size();
        }
        for (int i = start; i < end; i++) {
//            pageDateList.addAll(dateList);
            pageDateList.add(dateList.get(i));
        }
        // 分页记录数大于7，分页
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, end, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    private Schedule packageSchedule(Schedule schedule) {
        // 设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHosName(schedule.getHoscode()));
        schedule.getParam().put("depname", departService.getDepartName(schedule.getHoscode(), schedule.getDepcode()));
        schedule.getParam().put("dayOfWeek", getWeek(schedule.getWorkDate()));
        return schedule;
    }

    private DateTime getDateTime(Date date, String timeString) {
        return DateUtil.parse(new DateTime(date).toString("yyyy-MM-dd") + " " + timeString);
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
