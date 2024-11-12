package com.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.hosp.HospitalSet;
import com.yygh.vo.order.SignInfoVo;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service
 * @Description
 * @date 2024-09-03 19:55
 */
public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    SignInfoVo getSiggnInfoVo(String hoscode);
}
