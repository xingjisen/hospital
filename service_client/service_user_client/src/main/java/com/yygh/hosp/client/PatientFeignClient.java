package com.yygh.hosp.client;

import com.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.client
 * @Description
 * @date 2024-11-12 21:31
 */
@FeignClient(value = "service-user")
@Repository
public interface PatientFeignClient {

    /**
     * 根据就诊人id获取就诊人信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/api/user/patient/inner/get/{id}")
    public Patient getParentId(@PathVariable Long id);
}
