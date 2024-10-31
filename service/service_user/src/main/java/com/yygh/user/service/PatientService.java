package com.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.user.Patient;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service
 * @Description
 * @date 2024-10-31 20:22
 */
public interface PatientService extends IService<Patient> {
    List<Patient> listById(Long userId);

    int add(Patient patient);

    Patient getById(Long id);
}
