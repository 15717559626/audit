package com.snow.audit.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author lish
 * @Date 2025/6/18 16:07
 * @DESCRIBE
 */
@Data
public class ApprovalDetailVO {

    // 审批基本信息
    private Long id;
    private Long leaveId;
    private String type;
    private String status;
    private Integer currentStep;
    private Integer totalSteps;

    // 请假申请信息
    private Long applicantId;
    private String applicantName;
    private String department;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;
    private String leaveType;
    private String leaveTypeName;
    private String days;
    private String reason;
    private LocalDateTime applyTime;

    // 审批流程信息
    private List<ApprovalStepVO> approvalSteps;
}
