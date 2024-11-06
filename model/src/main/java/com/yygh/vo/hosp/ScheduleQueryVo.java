package com.yygh.vo.hosp;

import com.yygh.vo.common.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Schema(description = "Schedule")
public class ScheduleQueryVo extends Page {

    @Schema(description = "医院编号")
    private String hoscode;

    @Schema(description = "科室编号")
    private String depcode;

    @Schema(description = "医生编号")
    private String doccode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "安排日期")
    private Date workDate;

    @Schema(description = "安排时间（0：上午 1：下午）")
    private Integer workTime;

}

