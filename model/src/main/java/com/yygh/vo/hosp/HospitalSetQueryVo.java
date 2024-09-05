package com.yygh.vo.hosp;

import com.yygh.vo.common.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xingjisen
 */
@Data
public class HospitalSetQueryVo extends Page {

    @Schema(description = "医院名称")
    private String hosname;

    @Schema(description = "医院编号")
    private String hoscode;

}
