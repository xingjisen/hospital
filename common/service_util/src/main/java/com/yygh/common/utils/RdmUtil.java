package com.yygh.common.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.common.utils
 * @Description
 * @date 2024-10-21 21:17
 */
public final class RdmUtil {
    public static String random4Code() {
        return RandomUtil.randomNumbers(4);
    }

    public static String random6Code() {
        return RandomUtil.randomNumbers(6);
    }
}
