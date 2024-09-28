package com.yygh.model.cmn;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yygh.vo.common.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Dict
 * </p>
 *
 * @author qy
 */
@Data
@Schema(name = "数据字典")
@TableName("dict")
public class Dict extends Page {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @Schema(description = "逻辑删除(1:已删除，0:未删除)")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    @Schema(description = "其他参数")
    @TableField(exist = false)
    private Map<String, Object> param = new HashMap<>();

    @Schema(description = "上级id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "值")
    @TableField("value")
    private String value;

    @Schema(description = "编码")
    @TableField("dict_code")
    private String dictCode;

    @Schema(description = "是否包含子节点")
    @TableField(exist = false)
    private boolean hasChildren;


}