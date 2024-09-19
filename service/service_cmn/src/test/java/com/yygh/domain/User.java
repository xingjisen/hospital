package com.yygh.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.exay_excel.domain
 * @Description
 * @date 2024-09-13 15:01
 */
@Data
public class User {
    @ColumnWidth(10)
    @ExcelProperty("用户名称")
    private String name;
    
    @ColumnWidth(10)
    @ExcelProperty("用户年龄")
    private Integer age;
}
