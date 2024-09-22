package com.yygh.hosp.service;

import com.yygh.model.hosp.Department;
import com.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service
 * @Description
 * @date 2024-09-21 9:50
 */
public interface DepartService {
    void save(Map<String, Object> paramMap);

    Page<Department> list(DepartmentQueryVo departmentQueryVo);

    void remove(Department department);
}
