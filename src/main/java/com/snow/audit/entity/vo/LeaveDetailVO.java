package com.snow.audit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveDetailVO {

    private Long id;
    private Long applicantId;
    private String applicantName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String leaveTypeName;
    private String days;
    private String reason;
    private String status;
    private String statusName;
    private LocalDateTime applyTime;
    private Long approverId;
    private String approverName;
    private LocalDateTime approveTime;
    private String approveComment;
    private String attachmentUrl;
    private String department;
}
