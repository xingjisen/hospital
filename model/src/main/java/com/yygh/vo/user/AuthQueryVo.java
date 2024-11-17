package com.yygh.vo.user;

import com.yygh.vo.common.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户查询")
public class AuthQueryVo extends Page {
    @Schema(defaultValue = "昵称")
    private String nickName;
    @Schema(defaultValue = "手机号")
    private String phone;
    @Schema(defaultValue = "用户姓名")
    private String name;
    @Schema(defaultValue = "状态（0：锁定 1：正常）")
    private Integer status;


}
