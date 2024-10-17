package com.yygh.hosp.service;

import com.yygh.model.hosp.Hospital;
import com.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
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

    /**
     * 根据医院名称做查询
     */
    List<Hospital> findByHosname(String hosname);

    /**
     * 根据医院编号获取医院预约挂号详情
     */
    Map<String, Object> item(String hoscode);
}
