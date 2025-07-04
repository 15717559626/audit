package com.snow.audit.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author lish
 * @Date 2025/6/18 10:31
 * @DESCRIBE
 */
@Data
public class LeaveApplyParam {

    @NotNull(message = "申请人ID不能为空")
    private Long applicantId;

    @NotBlank(message = "申请人姓名不能为空")
    private String applicantName;

    @NotNull(message = "开始日期不能为空")
    private LocalDateTime startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDateTime endDate;

    @NotBlank(message = "请假类型不能为空")
    private String leaveType;

    @NotBlank(message = "请假类型名称不能为空")
    private String leaveTypeName;

    @NotBlank(message = "请假原因不能为空")
    @Length(max = 500, message = "请假原因不能超过500字")
    private String reason;

    @ApiModelProperty(value = "附件URL")
    private String attachmentUrl;

    @ApiModelProperty(value = "部门")
    private String department;

    @NotBlank(message = "请假天数")
    private String duration;

}
