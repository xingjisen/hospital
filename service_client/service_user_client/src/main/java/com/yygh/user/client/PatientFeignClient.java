package com.yygh.user.client;

import com.yygh.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.client
 * @Description
 * @date 2024-11-12 21:31
 */
@FeignClient(value = "service-user", path = "/api/user/patient")
@Repository
public interface PatientFeignClient {

    /**
     * 根据就诊人id获取就诊人信息
     *
     * @param id
     * @return
     */
    @GetMapping("/inner/get/{id}")
    public Result getParentId(@PathVariable Long id);
}
