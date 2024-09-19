package com.yygh.common.utils;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.common.utils
 * @Description
 * @date 2024-09-13 17:23
 */
public final class FileUtil {
    public static void setHeaderInfo(HttpServletResponse response, String fileName) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }
}
