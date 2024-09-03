package com.yygh.model.hosp;

import com.yygh.model.base.BaseMongoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * Department
 * </p>
 *
 * @author qy
 */
@Data
@Schema(name = "Department")
@Document("Department")
public class Department extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    @Indexed //普通索引
    private String hoscode;

    @Schema(description = "科室编号")
    @Indexed(unique = true) //唯一索引
    private String depcode;

    @Schema(description = "科室名称")
    private String depname;

    @Schema(description = "科室描述")
    private String intro;

    @Schema(description = "大科室编号")
    private String bigcode;

    @Schema(description = "大科室名称")
    private String bigname;

}

