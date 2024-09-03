package com.yygh.model.acl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yygh.model.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 角色权限
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Data
@Schema(name = "角色权限")
@TableName("acl_role_permission")
public class RolePermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "roleid")
    @TableField("role_id")
    private Long roleId;

    @Schema(description = "permissionId")
    @TableField("permission_id")
    private Long permissionId;

}

