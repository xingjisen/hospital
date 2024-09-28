package com.yygh.hosp.controller;

import cn.hutool.core.date.DateUtil;
import com.yygh.common.result.Result;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-27 11:03
 */
@RestController
@Tag(name = "排班信息查询")
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Operation(summary = "查询排班规则数据")
    @GetMapping("/getScheduleRule")
    public Result getScheduleRule(ScheduleQueryVo scheduleQueryVo) {
        Map<String, Object> map = scheduleService.getScheduleRule(scheduleQueryVo);
        return Result.success(map);
    }

    @Operation(summary = "查询排班详细数据")
    @GetMapping("/getDetailSchedule/{hoscode}/{depcode}/{workDate}")
    public Result getDetailSchedule(@PathVariable("hoscode") String hoscode, @PathVariable("depcode") String depcode, @PathVariable("workDate") String workDate) throws ParseException {
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        scheduleQueryVo.setWorkDate(DateUtil.parse(workDate));
        List<Schedule> list = scheduleService.getDetailSchedule(scheduleQueryVo);
        return Result.success(list);
    }
}
