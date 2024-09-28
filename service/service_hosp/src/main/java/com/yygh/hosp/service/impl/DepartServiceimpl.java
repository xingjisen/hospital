package com.yygh.hosp.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.alibaba.fastjson2.JSONObject;
import com.yygh.hosp.repository.DepartRepository;
import com.yygh.hosp.service.DepartService;
import com.yygh.model.hosp.Department;
import com.yygh.vo.hosp.DepartmentQueryVo;
import com.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    @Cacheable(value = "departTree", keyGenerator = "keyGenerator")
    public List<DepartmentVo> getDeptTree(String hoscode) {
        //创建list集合 用于数据封装
        List<DepartmentVo> result = new ArrayList<>();

        // 根据医院编号查询医院所有科室信息
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        List<Department> departList = departRepository.findAll(example);
        // 根据科室编号分组获取每个大科室下级子科室
        Map<String, List<Department>> departMap = departList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        // 将数据进行分组，大科室包裹二级科室
        for (Map.Entry<String, List<Department>> entry : departMap.entrySet()) {
            String bigCode = entry.getKey();
            List<Department> smallList = entry.getValue();
            // 封装大科室
            DepartmentVo bigDepartVo = new DepartmentVo();
            bigDepartVo.setDepcode(bigCode);
            bigDepartVo.setDepname(smallList.get(0).getBigname());
            // 封装二级科室集合
            List<DepartmentVo> smaillDepartVoList = Convert.convert(new TypeReference<List<DepartmentVo>>() {
            }, smallList);
            bigDepartVo.setChildren(smaillDepartVoList);
            result.add(bigDepartVo);
        }
        return result;
    }

    @Override
    public String getDepartName(String hoscode, String depcode) {
        Department department = departRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }
}
