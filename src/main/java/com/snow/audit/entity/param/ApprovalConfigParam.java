package com.snow.audit.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ApprovalConfigParam {

    @NotEmpty(message = "审批步骤不能为空")
    private List<ApprovalStepParam> steps;
}
