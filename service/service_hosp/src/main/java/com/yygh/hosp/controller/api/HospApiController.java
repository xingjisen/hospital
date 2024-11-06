package com.yygh.hosp.controller.api;

import com.yygh.common.result.Result;
import com.yygh.hosp.service.DepartService;
import com.yygh.hosp.service.HospitalService;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.vo.hosp.HospitalQueryVo;
import com.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller.api
 * @Description
 * @date 2024-10-13 21:20
 */
@RestController
@Tag(name = "前端预约平台接口")
@RequestMapping("/api/hosp/site")
public class HospApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartService departService;
    @Autowired
    private ScheduleService scheduleService;


    @Operation(summary = "查询医院列表")
    @GetMapping("/list")
    public Result list(HospitalQueryVo hospitalQueryVo) {
        return Result.success(hospitalService.list(hospitalQueryVo));
    }

    @Operation(summary = "根据医院名称查询")
    @GetMapping("/findByName/{hosname}")
    public Result findByName(@PathVariable("hosname") String hosname) {
        return Result.success(hospitalService.findByHosname(hosname));
    }

    @Operation(summary = "根据医院编号查询科室信息")
    @GetMapping("/dept/{hoscode}")
    public Result index(@PathVariable("hoscode") String hoscode) {
        return Result.success(departService.getDeptTree(hoscode));
    }

    @Operation(summary = "医院预约挂号详情信息")
    @GetMapping("/hospDetail/{hoscode}")
    public Result detail(@PathVariable("hoscode") String hoscode) {
        return Result.success(hospitalService.item(hoscode));
    }

    @Operation(summary = "获取可预约排班信息")
    @GetMapping("/auth/getBookingScheduleRule")
    public Result bookingScheduleRule(ScheduleQueryVo scheduleQueryVo) {
        return Result.success(scheduleService.bookingScheduleRule(scheduleQueryVo));
    }

    @Operation(summary = "获取排班信息")
    @GetMapping("/auth/findScheduleList")
    public Result findScheduleList(ScheduleQueryVo scheduleQueryVo) {
        return Result.success(scheduleService.getDetailSchedule(scheduleQueryVo));
    }
}
