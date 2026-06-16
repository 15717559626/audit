package com.snow.audit.entity.param;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairDiagnosisParam {

    @ApiModelProperty(value = "主键ID(更新时必填)")
    @JsonAlias("id")
    private Long id;

    @NotBlank(message = "工单编号不能为空")
    @ApiModelProperty(value = "工单编号", required = true)
    @JsonAlias("order_id")
    private String orderId;

    @NotBlank(message = "车辆型号不能为空")
    @ApiModelProperty(value = "车辆型号", required = true)
    @JsonAlias("vehicle")
    private String vehicle;

    @NotBlank(message = "故障现象不能为空")
    @ApiModelProperty(value = "故障现象", required = true)
    @JsonAlias("symptom")
    private String symptom;

    @ApiModelProperty(value = "故障码")
    @JsonAlias("dtc")
    private String dtc;

    @ApiModelProperty(value = "诊断结论")
    @JsonAlias("diagnosis")
    private String diagnosis;

    @ApiModelProperty(value = "维修方案")
    @JsonAlias("solution")
    private String solution;

    @ApiModelProperty(value = "更换配件")
    @JsonAlias("parts")
    private String parts;

    @ApiModelProperty(value = "维修工时(小时)")
    @JsonAlias("hours")
    private BigDecimal hours;

    @ApiModelProperty(value = "诊断置信度")
    @JsonAlias("confidence")
    private BigDecimal confidence;

    @ApiModelProperty(value = "验证结果")
    @JsonAlias("result_verification")
    private String resultVerification;
}
