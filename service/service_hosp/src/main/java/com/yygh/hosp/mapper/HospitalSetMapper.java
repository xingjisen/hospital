package com.yygh.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yygh.model.hosp.HospitalSet;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.mapper
 * @Description
 * @date 2024-09-03 19:42
 */
public interface HospitalSetMapper extends BaseMapper<HospitalSet> {
    String getSignKey(String hoscode);
}
