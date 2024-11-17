package com.yygh.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yygh.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户")
@TableName("auth")
public class Auth extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Schema(defaultValue = "昵称")
    @TableField("nick_name")
    private String nickName;

    @Schema(defaultValue = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(defaultValue = "账号")
    @TableField("user_name")
    private String userName;

    @Schema(defaultValue = "头像")
    @TableField("profile_photo")
    private String profilePhoto;

    @Schema(defaultValue = "密码")
    @TableField("password")
    private String password;

    @Schema(defaultValue = "状态（0：锁定 1：正常）")
    @TableField("status")
    private String status;
}
