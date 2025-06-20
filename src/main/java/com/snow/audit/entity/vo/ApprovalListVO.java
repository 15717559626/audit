package com.snow.audit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApprovalListVO {

    private Long id;
    private Long leaveId;
    private String type;
    private String status;
    private Integer currentStep;
    private Integer totalSteps;
    private String createTime;

    // 请假申请信息
    private String applicantName;
    private String leaveTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String reason;
}
