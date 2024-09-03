package com.yygh.model.hosp;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * RegisterRule
 * </p>
 *
 * @author qy
 */
@Data
@Schema(name = "预约规则")
@Document("BookingRule")
public class BookingRule {

    @Schema(description = "预约周期")
    private Integer cycle;

    @Schema(description = "放号时间")
    private String releaseTime;

    @Schema(description = "停挂时间")
    private String stopTime;

    @Schema(description = "退号截止天数（如：就诊前一天为-1，当天为0）")
    private Integer quitDay;

    @Schema(description = "退号时间")
    private String quitTime;

    @Schema(description = "预约规则")
    private List<String> rule;

    /**
     * @param rule
     */
    public void setRule(String rule) {
        if (!StringUtils.isEmpty(rule)) {
            this.rule = JSONArray.parseArray(rule, String.class);
        }
    }

}

