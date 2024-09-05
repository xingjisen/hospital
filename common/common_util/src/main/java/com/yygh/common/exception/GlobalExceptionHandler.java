package com.yygh.common.exception;

import com.yygh.common.result.Result;
import com.yygh.common.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.common.exception
 * @Description 全局异常处理类
 * @date 2024-09-05 21:33
 */
@ControllerAdvice
@Slf4j
@RestController
public class GlobalExceptionHandler {


    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        log.error("错误日志");
        System.out.println("捕获到异常: " + e.getMessage());
        e.printStackTrace();
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
        log.error("错误日志");
        System.out.println("捕获到自定义异常: " + e.getMessage());
        e.printStackTrace();
        return Result.fail();
    }
}
