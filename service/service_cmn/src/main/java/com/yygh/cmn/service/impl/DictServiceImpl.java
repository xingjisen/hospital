package com.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yygh.cmn.listener.DictListener;
import com.yygh.cmn.mapper.DictMapper;
import com.yygh.cmn.service.DictService;
import com.yygh.common.utils.FileUtil;
import com.yygh.model.cmn.Dict;
import com.yygh.vo.cmn.DictEeVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service.impl
 * @Description
 * @date 2024-09-03 19:56
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    public List<Dict> list(Dict dict) {
        return baseMapper.list(dict);
    }

    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> getParentIdDictList(Long id) {
        List<Dict> parentIdDictList = baseMapper.getParentIdDictList(id);
        parentIdDictList.forEach(dict -> {
            Boolean flag = hasChildren(dict.getId());
            dict.setHasChildren(flag);
        });
        return parentIdDictList;
    }

    @Override
    public void export(HttpServletResponse response) {
        String fileName = "dict";
        FileUtil.setHeaderInfo(response, fileName);
        // 查询数据库
        List<Dict> dicts = baseMapper.selectList(null);
        // dict转换dictVo
        List<DictEeVo> dictEeVos = dicts.stream().map(dict -> {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            return dictEeVo;
        }).toList();
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet().doWrite(dictEeVos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public Boolean delDict(Long id) {
        // 删除数据
        Integer isDelId = baseMapper.deleteById(id);
        // 删除子数据
        Integer isDelParentId = baseMapper.deleteByParentId(id);
        if (isDelId != 0 || isDelParentId != 0) {
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public Integer insert(Dict dict) {
        // 查询数据库最后一条数据的主键
        Long endId = baseMapper.getEndId();
        dict.setId(endId + 1);
        return baseMapper.insert(dict);
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public Integer update(Dict dict) {
        dict.setValue(!StringUtils.isEmpty(dict.getValue()) ? dict.getValue() : null);
        return baseMapper.updateById(dict);
    }

    @Override
    public Dict detailDict(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            // 根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            // 根据dictCode和value查询
            Dict codeDict = getDictByDictCode(dictCode);
            Long parentId = codeDict.getId();
            // 根据ParentId+value查询数据
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parentId)
                    .eq("value", value));
            return finalDict.getName();
        }
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        Dict dict = getDictByDictCode(dictCode);
        QueryWrapper<Dict> wrapper = new QueryWrapper();
        wrapper.eq("parent_id", dict.getId());
        return baseMapper.selectList(wrapper);
    }

    /**
     * 查询层级下是否有子节点
     */
    public Boolean hasChildren(Long id) {
        return baseMapper.hasChild(id);
    }
}
