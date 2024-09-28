package com.yygh.vo.common;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Jason
 * @PACKAGE_NAME com.yygh.vo.common
 * @Description
 * @date 2024-09-05 19:47
 */
@Data
public class Page {
    /**
     * 页数
     */
    @TableField(exist = false)
    @Schema(description = "分页数据")
    private Integer pageSize;
    /**
     * 页码
     */
    @TableField(exist = false)
    @Schema(description = "页码")
    private Integer pageNum;
}
