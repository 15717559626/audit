package com.snow.audit.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author lish
 * @Date 2025/6/18 16:08
 * @DESCRIBE
 */
@Data
public class ApprovalStepVO {

    private Integer stepOrder;
    private String name;
    private String description;
    private String approvalType;
    private List<ApproverVO> approvers;

    // 审批记录信息
    private Long approverId;
    private String approverName;
    private String result;
    private String comment;
    private LocalDateTime approveTime;
}
