package com.yygh.hosp.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.yygh.hosp.repository.HospitalRepository;
import com.yygh.hosp.service.HospitalService;
import com.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service.impl
 * @Description
 * @date 2024-09-20 11:51
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);

        String hoscode = hospital.getHoscode();
        Hospital byHosp = hospitalRepository.getHospitalByHoscode(hoscode);

        if (byHosp != null) {
            hospital.setId(byHosp.getId());
            hospital.setStatus(byHosp.getStatus());
            hospital.setCreateTime(byHosp.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(byHosp.getIsDeleted());
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
        }
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }
}
