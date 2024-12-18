package com.yygh.vo.order;

import com.yygh.vo.sms.SmsVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author xingjisen
 */
@Data
@Schema(description = "OrderMqVo")
public class OrderMqVo {

    @Schema(description = "可预约数")
    private Integer reservedNumber;

    @Schema(description = "剩余预约数")
    private Integer availableNumber;

    @Schema(description = "排班id")
    private String scheduleId;

    @Schema(description = "短信实体")
    private SmsVo smsVo;

}

