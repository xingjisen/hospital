package com.yygh.hosp.repository;

import com.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.repository
 * @Description
 * @date 2024-09-20 11:48
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    /**
     * 继承MongoRepository 符合Spring Data规范即可，不需要写方法实现
     *
     * @param hoscode
     * @return
     */
    Hospital getHospitalByHoscode(String hoscode);

    /**
     * 继承MongoRepository 符合Spring Data规范即可，不需要写方法实现
     *
     * @param hosname
     * @return
     */
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
