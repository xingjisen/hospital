package com.yygh.cmn.controller;

import com.yygh.cmn.service.DictService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.controller
 * @Description
 * @date 2024-09-03 20:01
 */
@Tag(name = "医院设置管理")
@RestController
@RequestMapping("/admin/cmn/dict")
public class HospitalSetController {
    @Autowired
    DictService dictService;

}
