package com.yygh.cmn.controller;

import com.yygh.cmn.service.DictService;
import com.yygh.common.result.Result;
import com.yygh.model.cmn.Dict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-11 20:01
 */
@Tag(name = "字典管理")
@RestController
@CrossOrigin
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    DictService dictService;

    @Operation(summary = "查询父节点下数据")
    @GetMapping("/{id}")
    public Result listByParentId(@PathVariable("id") Long id) {
        return Result.success(dictService.getParentIdDictList(id));
    }

    @Operation(summary = "查询节点详情数据")
    @GetMapping("/detail/{id}")
    public Result detailDict(@PathVariable("id") Long id) {
        return Result.success(dictService.detailDict(id));
    }

    @Operation(summary = "删除数据")
    @DeleteMapping("/{id}")
    public Result delDict(@PathVariable("id") Long id) {
        Boolean flag = dictService.delDict(id);
        return flag ? Result.success() : Result.fail();
    }

    @Operation(summary = "新增数据")
    @PostMapping
    public Result addDict(@RequestBody Dict dict) {
        Integer flag = dictService.insert(dict);
        return flag != null ? Result.success() : Result.fail();
    }

    @Operation(summary = "修改数据")
    @PutMapping
    public Result putDict(@RequestBody Dict dict) {
        Integer flag = dictService.update(dict);
        return flag != null ? Result.success() : Result.fail();
    }

    @Operation(summary = "清除缓存")
    @GetMapping("/clearCache")
    @CacheEvict(value = "dict", allEntries = true)
    public Result clearCache() {
        return Result.success();
    }

    @Operation(summary = "导出数据字典")
    @GetMapping("/export")
    public Result export(HttpServletResponse response) {
        dictService.export(response);
        return Result.success();
    }

    @Operation(summary = "导入数据字典")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        try {
            dictService.upload(file);
            return Result.success();
        } catch (IOException e) {
            System.out.println(new RuntimeException(e));
            return Result.fail();
        }
    }

}
