package com.yygh.hosp.controller;

import com.yygh.hosp.service.HospitalSetService;
import com.yygh.model.hosp.HospitalSet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-03 20:01
 */
@Tag(name = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    HospitalSetService hospitalSetService;

    @Operation(summary = "获取所有医院设置信息")
    @GetMapping("list")
    public List<HospitalSet> list() {
        return hospitalSetService.list();
    }

    @Operation(summary = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return hospitalSetService.removeById(id);
    }
}
