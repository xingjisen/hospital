package com.yygh.common.exception;

import com.yygh.common.result.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 自定义全局异常类
 *
 * @author qy
 */
@Data
@Schema(pattern = "自定义全局异常类")
public class YyghException extends RuntimeException {

    @Schema(pattern = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message
     * @param code
     */
    public YyghException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     *
     * @param resultCodeEnum
     */
    public YyghException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "YyghException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
