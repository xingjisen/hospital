package com.yygh.common.exception;

import com.yygh.common.result.Result;
import com.yygh.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.common.exception
 * @Description 全局异常处理类
 * @date 2024-09-05 21:33
 */
//@RestControllerAdvice
@ControllerAdvice
@Slf4j
@Component
public class GlobalExceptionHandler {


    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @Order(-2)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
//        log.error("错误日志");
//        System.out.println("捕获到异常: " + e.getMessage());
//        e.printStackTrace();
        log.error("捕获到系统异常: {}", e.getMessage(), e);  // 记录具体自定义异常信息
        return Result.fail(ResultCodeEnum.ERROR.getCode(), e.getMessage());
    }

    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result CustomError(YyghException e) {
//        log.error("错误日志");
//        System.out.println("捕获到自定义异常: " + e.getMessage());
//        e.printStackTrace();
//        return Result.fail();
        log.error("捕获到自定义异常: {}", e.getMessage(), e);  // 记录具体自定义异常信息
        return Result.fail(e.getCode(), e.getMessage());  // 返回自定义异常中的状态码和错误信息
    }
}
