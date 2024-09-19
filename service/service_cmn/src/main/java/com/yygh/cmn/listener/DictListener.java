package com.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.yygh.cmn.mapper.DictMapper;
import com.yygh.model.cmn.Dict;
import com.yygh.vo.cmn.DictEeVo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.cmn.listener
 * @Description 文件读取监听器
 * @date 2024-09-13 19:28
 */
@AllArgsConstructor
@NoArgsConstructor
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    /**
     * 一行一行读取
     *
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);
    }

    /**
     * 读取完成时的操作
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
