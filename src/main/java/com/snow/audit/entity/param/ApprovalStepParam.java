package com.snow.audit.entity.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author lish
 * @Date 2025/6/18 16:08
 * @DESCRIBE
 */
@Data
public class ApprovalStepParam {

    @NotBlank(message = "步骤名称不能为空")
    private String name;

    private String description;

    private String approvalType; // sequential, parallel

    @NotEmpty(message = "审批人不能为空")
    private List<ApproverParam> approvers;
}
