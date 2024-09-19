package com.yygh.cmn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yygh.model.cmn.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.mapper
 * @Description
 * @date 2024-09-03 19:42
 */
public interface DictMapper extends BaseMapper<Dict> {
    List<Dict> getParentIdDictList(@Param("parentId") Long parentId);

    Boolean hasChild(@Param("parentId") Long parentId);

    Integer deleteById(@Param("id") Long id);

    Integer deleteByParentId(@Param("parentId") Long parentId);

    Long getEndId();
}
