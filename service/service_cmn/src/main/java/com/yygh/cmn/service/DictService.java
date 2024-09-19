package com.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yygh.model.cmn.Dict;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.hosp.service
 * @Description
 * @date 2024-09-03 19:55
 */
public interface DictService extends IService<Dict> {
    /**
     * 查询父节点下数据
     *
     * @param id
     * @return
     */
    List<Dict> getParentIdDictList(Long id);

    /**
     * 导出数据字典
     *
     * @param response
     */
    void export(HttpServletResponse response);

    /**
     * 导入数据字典
     */
    void upload(MultipartFile file) throws IOException;

    Boolean delDict(Long id);

    Integer insert(Dict dict);

    Integer update(Dict dict);

    Dict detailDict(Long id);
}
