package com.yygh.hosp.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSONObject;
import com.yygh.hosp.repository.DepartRepository;
import com.yygh.hosp.service.DepartService;
import com.yygh.model.hosp.Department;
import com.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service.impl
 * @Description
 * @date 2024-09-21 9:50
 */
@Service
public class DepartServiceimpl implements DepartService {
    @Autowired
    DepartRepository departRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        // 转换为String
        String jsonString = JSONObject.toJSONString(paramMap);
        // 转换为实体类
        Department department = JSONObject.parseObject(jsonString, Department.class);
        // 查询是否有内容，有则不上传
        String depcode = department.getDepcode();
//        Department byDepart = departRepository.getDepartmentByDepcode(depcode);
        Department byDepart = departRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (byDepart != null) {
            department.setId(byDepart.getId());
            department.setUpdateTime(new Date());
            department.setCreateTime(byDepart.getCreateTime());
            department.setIsDeleted(byDepart.getIsDeleted());
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
        }
        departRepository.save(department);

    }

    @Override
    public Page<Department> list(DepartmentQueryVo departmentQueryVo) {
        Pageable pageable = PageRequest.of(departmentQueryVo.getPageNum() - 1, departmentQueryVo.getPageSize());
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Department department = Convert.convert(Department.class, departmentQueryVo);
        Example<Department> example = Example.of(department, matcher);
        return departRepository.findAll(example, pageable);
    }

    @Override
    public void remove(Department department) {
        Department departmentByHoscodeAndDepcode = departRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (departmentByHoscodeAndDepcode != null) {
            departRepository.deleteById(departmentByHoscodeAndDepcode.getId());
        }
    }
}
