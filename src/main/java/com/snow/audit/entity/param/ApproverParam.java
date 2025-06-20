package com.snow.audit.entity.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author lish
 * @Date 2025/6/19 14:40
 * @DESCRIBE
 */
@Data
public class ApproverParam implements Serializable {

    @NotBlank(message = "审批人ID不能为空")
    private Long id;

    @NotBlank(message = "审批人姓名不能为空")
    private String name;

    private String department;
    private String position;
}
