package com.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yygh.common.result.Result;
import com.yygh.common.utils.MD5;
import com.yygh.hosp.service.HospitalSetService;
import com.yygh.model.hosp.HospitalSet;
import com.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
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
    public Result list() {
        return Result.success(hospitalSetService.list());
    }

    @Operation(summary = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result del(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            Result.success();
        }
        return Result.fail();
    }

    @Operation(summary = "分页逻辑查询医院设置")
    @PostMapping("/findPage")
    public Result findPage(@RequestBody HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> page = new Page<>(hospitalSetQueryVo.getPageSize(), hospitalSetQueryVo.getPageNum());
        // 搜索条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode", hoscode);
        }
        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.success(hospitalSetPage);
    }

    @Operation(summary = "添加医院设置")
    @PostMapping("/add")
    public Result add(@RequestBody HospitalSet hospitalSet) {
        SecureRandom random = new SecureRandom();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean flag = hospitalSetService.save(hospitalSet);
        if (!flag) {
            return Result.fail("新增失败");
        }
        return Result.success();
    }

    @Operation(summary = "查看医院设置详情")
    @GetMapping("{id}")
    public Result getById(@PathVariable Long id) {
        if (id == null) {
            return Result.fail("id为空");
        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.success(hospitalSet);
    }

    @Operation(summary = "修改医院设置")
    @PutMapping
    public Result put(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (!flag) {
            return Result.fail();
        }
        return Result.success();
    }

    @Operation(summary = "批量删除医院设置")
    @DeleteMapping("/dels")
    public Result dels(@RequestBody List<Long> ids) {
        boolean flag = hospitalSetService.removeByIds(ids);
        if (!flag) {
            return Result.fail();
        }
        return Result.success();
    }

    @Operation(summary = "医院设置锁定和解锁")
    @PutMapping("/lock/{id}/{status}")
    public Result lock(@PathVariable Long id, @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.success();
    }

    @Operation(summary = "发送签名密钥")
    @PostMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        // TODO 发送短信
        return Result.success();
    }
}
