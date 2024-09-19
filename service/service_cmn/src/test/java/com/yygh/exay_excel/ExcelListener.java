package com.yygh.exay_excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.yygh.domain.User;

import java.util.Map;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.exay_excel
 * @Description
 * @date 2024-09-13 15:25
 */
public class ExcelListener extends AnalysisEventListener<User> {
    /*
     * 一行一行读取
     * */
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        System.out.println(user);
    }

    /*
     * 表头信息
     * */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息" + headMap);
    }

    /*
     * 读取完成执行
     * */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
