package com.snow.audit.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RepairDiagnosisVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "工单编号")
    private String orderId;

    @ApiModelProperty(value = "车辆型号")
    private String vehicle;

    @ApiModelProperty(value = "故障现象")
    private String symptom;

    @ApiModelProperty(value = "故障码")
    private String dtc;

    @ApiModelProperty(value = "诊断结论")
    private String diagnosis;

    @ApiModelProperty(value = "维修方案")
    private String solution;

    @ApiModelProperty(value = "更换配件")
    private String parts;

    @ApiModelProperty(value = "维修工时(小时)")
    private BigDecimal hours;

    @ApiModelProperty(value = "诊断置信度")
    private BigDecimal confidence;

    @ApiModelProperty(value = "验证结果")
    private String resultVerification;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
