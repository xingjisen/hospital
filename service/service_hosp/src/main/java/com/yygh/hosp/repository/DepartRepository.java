package com.yygh.hosp.repository;

import com.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.repository
 * @Description
 * @date 2024-09-21 9:52
 */
public interface DepartRepository extends MongoRepository<Department, String> {
    /**
     * 继承MongoRepository 符合Spring Data规范即可，不需要写方法实现
     *
     * @param depcode
     * @return
     */
    Department getDepartmentByDepcode(String depcode);

    /**
     * 继承MongoRepository 符合Spring Data规范即可，不需要写方法实现
     *
     * @param hoscode
     * @return
     */
    Department getDepartmentByHoscode(String hoscode);

    /**
     * 继承MongoRepository 符合Spring Data规范即可，不需要写方法实现
     *
     * @param hoscode
     * @return
     */
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
