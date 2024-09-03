package com.yygh.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yygh.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * UserLoginRecordW
 * </p>
 *
 * @author qy
 */
@Data
@Schema(name = "用户登录日志")
@TableName("user_login_record")
public class UserLoginRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "ip")
    @TableField("ip")
    private String ip;

}

