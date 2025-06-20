package com.snow.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_bu_approval")
public class Approval {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 请假申请ID
     */
    private Long leaveId;

    /**
     * 审批类型(leave-请假)
     */
    private String type;

    /**
     * 审批状态(pending-待审批,approved-已通过,rejected-已拒绝)
     */
    private String status;

    /**
     * 当前审批步骤
     */
    private Integer currentStep;

    /**
     * 总审批步骤数
     */
    private Integer totalSteps;

    /**
     * 当前审批人ID
     */
    private Long currentApproverId;

    /**
     * 当前审批人姓名
     */
    private String currentApproverName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
