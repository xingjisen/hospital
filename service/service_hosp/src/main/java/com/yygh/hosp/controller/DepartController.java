package com.yygh.hosp.controller;

import com.yygh.common.result.Result;
import com.yygh.hosp.service.DepartService;
import com.yygh.vo.hosp.DepartmentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-26 21:33
 */
@RestController
@Tag(name = "查询科室")
@RequestMapping("/admin/hosp/depart")
public class DepartController {

    @Autowired
    private DepartService departService;

    @Operation(summary = "查询医院所有科室列表")
    @GetMapping("/getDeptTree/{hoscode}")
    public Result getDeptTree(@PathVariable String hoscode) {
        List<DepartmentVo> list = departService.getDeptTree(hoscode);
        return Result.success(list);
    }
}
