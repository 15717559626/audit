package com.snow.audit.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ApprovalProcessParam {

    @NotNull(message = "审批ID不能为空")
    private Long id;

    @NotBlank(message = "审批人ID不能为空")
    private Long approverId;

    @NotBlank(message = "审批人姓名不能为空")
    private String approverName;

    @NotBlank(message = "审批状态不能为空")
    private String status; // approved, rejected

    private String comment; // 审批意见
}
