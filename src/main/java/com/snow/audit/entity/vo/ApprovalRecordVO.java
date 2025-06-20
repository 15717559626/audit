package com.snow.audit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApprovalRecordVO {

    private Long id;
    private Long leaveId;
    private Long approverId;
    private String approverName;
    private String result;
    private String comment;
    private String approveTime;
    private String createTime;

    private String applicantId;
    private String applicantName;
    private String department;
    private String leaveType;
    private String leaveTypeName;
    private String status;
    private String statusName;
    private String applyTime;
    private String days;




}
