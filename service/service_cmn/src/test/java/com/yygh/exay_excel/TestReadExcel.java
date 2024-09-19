package com.yygh.exay_excel;

import com.alibaba.excel.EasyExcel;
import com.yygh.domain.User;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.exay_excel
 * @Description
 * @date 2024-09-13 15:21
 */
public class TestReadExcel {
    public static void main(String[] args) {
        String fileName = "D://output.xls";
        EasyExcel.read(fileName, User.class, new ExcelListener()).sheet("用户信息").doRead();
    }
}
