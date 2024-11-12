package com.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.common.exception.YyghException;
import com.yygh.common.result.ResultCodeEnum;
import com.yygh.hosp.mapper.HospitalSetMapper;
import com.yygh.hosp.service.HospitalSetService;
import com.yygh.model.hosp.HospitalSet;
import com.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service.impl
 * @Description
 * @date 2024-09-03 19:56
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
    @Override
    public String getSignKey(String hoscode) {
        return baseMapper.getSignKey(hoscode);
    }

    @Override
    public SignInfoVo getSiggnInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if (hospitalSet == null) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;
    }
}
