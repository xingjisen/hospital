package com.yygh.hosp.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.yygh.cmn.client.DictFeignClient;
import com.yygh.common.utils.genericity.MongoUtil;
import com.yygh.hosp.repository.HospitalRepository;
import com.yygh.hosp.service.HospitalService;
import com.yygh.model.hosp.Hospital;
import com.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private DictFeignClient dictFeignClient;

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

    @Override
    public Page<Hospital> list(HospitalQueryVo hospitalQueryVo) {
        Pageable pageable = MongoUtil.buildPageable(hospitalQueryVo.getPageNum(), hospitalQueryVo.getPageSize());
        Example<Hospital> example = MongoUtil.buildExample(hospitalQueryVo, Hospital.class);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        // 获取查询list集合，遍历医院查询等级封装数据
        pages.forEach(this::setHospitalHosType);
        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Hospital findById(String id) {
        return setHospitalHosType(hospitalRepository.findById(id).get());
    }

    @Override
    public String getHosName(String hoscode) {
        Hospital byHoscode = hospitalRepository.getHospitalByHoscode(hoscode);
        if (byHoscode != null) {
            return byHoscode.getHosname();
        }
        return null;
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = setHospitalHosType(getByHoscode(hoscode));
        result.put("hospital", hospital);
        result.put("bookingRule", hospital.getBookingRule());
        return result;
    }


    public Hospital setHospitalHosType(Hospital hospital) {
        // 根据dictCode和value获取医院等级
        String hostype = dictFeignClient.getName("Hostype", hospital.getHostype());
        // 获取省市县
        String province = dictFeignClient.getName(hospital.getProvinceCode());
        String city = dictFeignClient.getName(hospital.getCityCode());
        String district = dictFeignClient.getName(hospital.getDistrictCode());

        hospital.getParam().put("area", province + city + district);
        hospital.getParam().put("hostype", hostype);
        return hospital;
    }
}
