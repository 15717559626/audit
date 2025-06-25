package com.snow.audit.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ApprovalListVO {

    private Long id;
    private Long leaveId;
    private String type;
    private String status;
    private Integer currentStep;
    private Integer totalSteps;
    private LocalDateTime createTime;

    // 请假申请信息
    private String applicantName;
    private String department;
    private String leaveTypeName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    private String days;
    private String reason;
    private LocalDateTime applyTime;
}
