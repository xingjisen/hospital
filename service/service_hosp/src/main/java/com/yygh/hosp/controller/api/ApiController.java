package com.yygh.hosp.controller.api;

import cn.hutool.core.convert.Convert;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.Result;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.common.utils.MD5;
import com.yygh.common.utils.helper.HttpRequestHelper;
import com.yygh.hosp.service.DepartService;
import com.yygh.hosp.service.HospitalService;
import com.yygh.hosp.service.HospitalSetService;
import com.yygh.hosp.service.ScheduleService;
import com.yygh.model.hosp.Department;
import com.yygh.model.hosp.Hospital;
import com.yygh.model.hosp.Schedule;
import com.yygh.vo.hosp.DepartmentQueryVo;
import com.yygh.vo.hosp.ScheduleOrderVo;
import com.yygh.vo.hosp.ScheduleQueryVo;
import com.yygh.vo.order.SignInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-20 19:11
 */
@RestController
@Tag(name = "医院接口")
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartService departService;
    @Autowired
    private ScheduleService scheduleService;


    /**
     * 上传医院接口
     */
    @Operation(summary = "上传医院接口")
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        String logoData = (String) paramMap.get("logoData");
        String imgBase64 = logoData.replaceAll(" ", "+");
        paramMap.put("logoData", imgBase64);

        hospitalService.save(paramMap);
        return Result.success();
    }

    /**
     * 查询医院接口
     */
    @Operation(summary = "查询医院接口")
    @PostMapping("/hospital/show")
    public Result lockHosp(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = isSign(paramMap);

        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.success(hospital);
    }


    /**
     * 上传科室接口
     */
    @Operation(summary = "上传科室接口")
    @PostMapping("/saveDepartment")
    public Result saveDepart(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        departService.save(paramMap);
        return Result.success();
    }

    /**
     * 查询科室接口
     */
    @Operation(summary = "查询科室接口")
    @PostMapping("/department/list")
    public Result lockDepart(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        paramMap.put("pageSize", paramMap.get("limit") != null ? paramMap.get("limit") : 1);
        paramMap.put("pageNum", paramMap.get("page") != null ? paramMap.get("page") : 1);

        DepartmentQueryVo departmentQueryVo = Convert.convert(DepartmentQueryVo.class, paramMap);

        return Result.success(departService.list(departmentQueryVo));
    }

    /**
     * 删除科室接口
     */
    @Operation(summary = "删除科室接口")
    @PostMapping("/department/remove")
    public Result delDepart(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        Department department = Convert.convert(Department.class, paramMap);

        departService.remove(department);
        return Result.success();
    }


    /**
     * 上传排班接口
     */
    @Operation(summary = "上传排班接口")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        scheduleService.save(paramMap);
        return Result.success();
    }

    /**
     * 查询排班接口
     */
    @Operation(summary = "查询排班接口")
    @PostMapping("/schedule/list")
    public Result lockSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        paramMap.put("pageSize", paramMap.get("limit") != null ? paramMap.get("limit") : 1);
        paramMap.put("pageNum", paramMap.get("page") != null ? paramMap.get("page") : 1);

        ScheduleQueryVo scheduleQueryVo = Convert.convert(ScheduleQueryVo.class, paramMap);

        return Result.success(scheduleService.list(scheduleQueryVo));
    }

    /**
     * 删除排班接口
     */
    @Operation(summary = "删除排班接口")
    @PostMapping("/schedule/remove")
    public Result delSchedule(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        isSign(paramMap);

        Schedule schedule = Convert.convert(Schedule.class, paramMap);

        scheduleService.remove(schedule);
        return Result.success();
    }


    /**
     * 判断签名是否一致
     */
    @Operation(summary = "判断签名是否一致")
    public String isSign(Map<String, Object> paramMap) {
        // 判断签名是否一致
        String sign = (String) paramMap.get("sign");

        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);

        // 判断签名
        if (!sign.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return hoscode;
    }

    @Operation(summary = "根据排班ID获取预约下单数据")
    @GetMapping("/inner/getSheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getSheduleOrderVo(@PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getSheduleOrderVo(scheduleId);
    }

    @Operation(summary = "根据排医院编号获取签名信息")
    @GetMapping("/inner/getSiggnInfoVo/{hoscode}")
    public SignInfoVo getSiggnInfoVo(@PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSiggnInfoVo(hoscode);
    }
}
