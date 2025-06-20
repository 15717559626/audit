package com.snow.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author lish
 * @Date 2025/6/19 14:34
 * @DESCRIBE
 */
@Data
@TableName("t_bu_approval_record")
public class ApprovalRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 请假申请ID
     */
    private Long leaveId;

    /**
     * 审批步骤顺序
     */
    private Integer stepOrder;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 审批结果(approved-通过,rejected-拒绝)
     */
    private String result;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
