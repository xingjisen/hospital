package com.yygh.user.controller.api;

import com.yygh.common.result.Result;
import com.yygh.common.utils.AuthContextUtil;
import com.yygh.model.user.Patient;
import com.yygh.user.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.api
 * @Description
 * @date 2024-10-31 20:21
 */
@Tag(name = "就诊人管理接口")
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {
    @Autowired
    private PatientService patientService;

    @Operation(summary = "获取就诊人列表")
    @GetMapping("/auth/list")
    public Result list() {
        Long userId = AuthContextUtil.getUserId();
        List<Patient> patients = patientService.listById(userId);
        return Result.success(patients);
    }

    @Operation(summary = "添加就诊人")
    @PostMapping("/auth/add")
    public Result add(@RequestBody Patient patient) {
        Long userId = AuthContextUtil.getUserId();
        patient.setUserId(userId);
        int add = patientService.add(patient);
        if (add > 0) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @Operation(summary = "根据id获取就诊人信息")
    @GetMapping("/auth/getById/{id}")
    public Result getById(@PathVariable Long id) {
        Patient patient = patientService.getById(id);
        return Result.success(patient);
    }

    @Operation(summary = "修改就诊人")
    @PutMapping("/auth/put")
    public Result put(@RequestBody Patient patient) {
        boolean b = patientService.updateById(patient);
        if (b) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @Operation(summary = "删除就诊人")
    @DeleteMapping("/auth/del/{id}")
    public Result del(@PathVariable String id) {
        boolean b = patientService.removeById(id);
        if (b) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @Operation(summary = "根据就诊人id获取就诊人信息")
    @DeleteMapping("/inner/get/{id}")
    public Result getParentId(@PathVariable Long id) {
        Patient patient = patientService.getById(id);
        return Result.success(patient);
    }
}
