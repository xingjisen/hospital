package com.yygh.hosp.service;

import com.yygh.model.hosp.Hospital;
import com.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service
 * @Description
 * @date 2024-09-20 11:50
 */
public interface HospitalService {
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);

    Page<Hospital> list(HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Hospital findById(String id);

    String getHosName(String hoscode);
}
