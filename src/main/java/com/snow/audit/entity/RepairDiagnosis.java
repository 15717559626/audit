package com.snow.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_bu_repair_diagnosis")
public class RepairDiagnosis {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "工单编号", required = true)
    private String orderId;

    @ApiModelProperty(value = "车辆型号", required = true)
    private String vehicle;

    @ApiModelProperty(value = "故障现象", required = true)
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
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
