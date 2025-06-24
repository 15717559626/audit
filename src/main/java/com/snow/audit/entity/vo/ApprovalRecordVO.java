package com.snow.audit.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalRecordVO {

    private Long id;
    private Long leaveId;
    private Long approverId;
    private String approverName;
    private String result;
    private String comment;
    private LocalDateTime approveTime;
    private String createTime;

    private String applicantId;
    private String applicantName;
    private String department;
    private String leaveType;
    private String leaveTypeName;
    private String status;
    private String statusName;
    private LocalDateTime applyTime;
    private String days;




}
