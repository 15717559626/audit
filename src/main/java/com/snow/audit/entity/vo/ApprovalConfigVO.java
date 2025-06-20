package com.snow.audit.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalConfigVO {

    private List<ApprovalStepVO> steps;

    private Boolean enabled;

}
