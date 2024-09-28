package com.yygh.hosp.controller;

import com.yygh.common.result.Result;
import com.yygh.hosp.service.HospitalService;
import com.yygh.model.hosp.Hospital;
import com.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-23 21:38
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
@Tag(name = "医院设置")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    /**
     * 查询
     *
     * @param hospitalQueryVo
     * @return
     */
    @Operation(summary = "查询数据")
    @GetMapping("/list")
    public Result list(HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> list = hospitalService.list(hospitalQueryVo);
        return Result.success(list);
    }

    /**
     * 更新医院状态
     *
     * @param id
     * @param status
     * @return
     */
    @Operation(summary = "更新医院状态")
    @PutMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 医院详情信息
     *
     * @param id
     * @return
     */
    @Operation(summary = "医院详情信息")
    @GetMapping("/showDetail/{id}")
    public Result showDetail(@PathVariable("id") String id) {
        Hospital hospital = hospitalService.findById(id);
        return Result.success(hospital);
    }
}
