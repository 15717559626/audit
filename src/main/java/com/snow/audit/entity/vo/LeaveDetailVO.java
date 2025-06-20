package com.snow.audit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDetailVO {

    private Long id;
    private Long applicantId;
    private String applicantName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String leaveTypeName;
    private Integer days;
    private String reason;
    private String status;
    private String statusName;
    private String applyTime;
    private Long approverId;
    private String approverName;
    private String approveTime;
    private String approveComment;
}
