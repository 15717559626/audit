package com.snow.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.audit.entity.ApprovalRecord;
import com.snow.audit.entity.param.ApprovalRecordParam;
import com.snow.audit.entity.vo.ApprovalRecordVO;

import java.util.List;

/**
 * 审批记录Service接口
 */
public interface ApprovalRecordService extends IService<ApprovalRecord> {

    /**
     * 根据请假ID获取审批记录
     */
    List<ApprovalRecord> getRecordsByLeaveId(Long leaveId);

    /**
     * 获取用户的审批记录
     */
    List<ApprovalRecordVO> getRecordsByApproverId(ApprovalRecordParam approvalRecordParam);
}

