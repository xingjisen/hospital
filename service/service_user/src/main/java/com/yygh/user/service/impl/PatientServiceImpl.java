package com.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.cmn.client.DictFeignClient;
import com.yygh.enums.DictEnum;
import com.yygh.model.user.Patient;
import com.yygh.user.mapper.PatientMapper;
import com.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.user.service.impl
 * @Description
 * @date 2024-10-31 20:22
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public List<Patient> listById(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Patient> patients = baseMapper.selectList(wrapper);
        patients.forEach(patient -> {
            packaPatient(patient);
        });

        return patients;
    }

    @Override
    public int add(Patient patient) {
        int insert = baseMapper.insert(patient);
        return insert;
    }

    @Override
    public Patient getById(Long id) {
        return packaPatient(baseMapper.selectById(id));
    }


    public Patient packaPatient(Patient patient) {
        // 根据证件类型编码获取证件类型值
        String certificatesType = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        // 联系人证件类型
        String contactsCertificate = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        // 省
        String providerCertificate = dictFeignClient.getName(patient.getProvinceCode());
        // 市
        String cityCertificate = dictFeignClient.getName(patient.getCityCode());
        // 区
        String districtCertificate = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesType);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificate);
        patient.getParam().put("providerCertificate", providerCertificate);
        patient.getParam().put("cityCertificate", cityCertificate);
        patient.getParam().put("districtCertificate", districtCertificate);
        patient.getParam().put("fullAddress", providerCertificate + cityCertificate + districtCertificate + patient.getAddress());
        return patient;


    }
}
