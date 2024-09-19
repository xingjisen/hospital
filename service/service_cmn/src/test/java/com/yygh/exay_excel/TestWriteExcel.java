package com.yygh.exay_excel;

import com.alibaba.excel.EasyExcel;
import com.yygh.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.exay_excel
 * @Description
 * @date 2024-09-13 15:02
 */
public class TestWriteExcel {
    public static void main(String[] args) {
        /** 创建待写入信息 */
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName("邢继森" + (i + 1));
            user.setAge(i + 1);
            users.add(user);
        }
        String fileName = "D://output.xls";
        EasyExcel.write(fileName, User.class).sheet("用户信息").doWrite(users);
    }
}
