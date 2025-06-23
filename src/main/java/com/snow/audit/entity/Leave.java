package com.snow.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_bu_leave")
public class Leave {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "请假单唯一标识")
    private Long id;

    @ApiModelProperty(value = "申请人ID", required = true)
    private Long applicantId;

    @ApiModelProperty(value = "申请人姓名")
    private String applicantName;

    @ApiModelProperty(value = "开始日期", required = true)
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期", required = true)
    private LocalDate endDate;

    @ApiModelProperty(value = "请假类型", required = true)
    private String leaveType;

    @ApiModelProperty(value = "请假类型名称")
    private String leaveTypeName;

    @ApiModelProperty(value = "请假天数", required = true)
    private Integer days;

    @ApiModelProperty(value = "请假原因", required = true, notes = "长度不超过500")
    private String reason;

    @ApiModelProperty(value = "请假单状态", required = true, notes = "可选值：pending, approved, rejected, cancelled")
    private String status; // pending, approved, rejected, cancelled

    @ApiModelProperty(value = "审批人ID")
    private Long approverId;

    @ApiModelProperty(value = "审批人姓名")
    private String approverName;

    @ApiModelProperty(value = "审批时间")
    private LocalDateTime approveTime;

    @ApiModelProperty(value = "审批意见")
    private String approveComment;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "附件图片url")
    private String attachmentUrl;
}