package com.snow.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author lish
 * @Date 2025/6/18 17:36
 * @DESCRIBE
 */
@Data
@TableName("t_bu_approval_config")
public class ApprovalConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审批步骤顺序
     */
    private Integer stepOrder;

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * 步骤描述
     */
    private String description;

    /**
     * 审批类型(sequential-顺序审批,parallel-并行审批)
     */
    private String approvalType;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 部门
     */
    private String department;

    /**
     * 职位
     */
    private String position;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
