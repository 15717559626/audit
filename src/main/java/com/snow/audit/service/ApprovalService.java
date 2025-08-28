package com.snow.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.param.ApprovalProcessParam;
import com.snow.audit.entity.param.ApprovalRecordParam;
import com.snow.audit.entity.vo.ApprovalDetailVO;
import com.snow.audit.entity.vo.ApprovalListVO;
import com.snow.audit.entity.vo.ApprovalRecordVO;


import java.util.List;

public interface ApprovalService extends IService<Approval> {
    /**
     * 获取待审批列表
     */
    List<ApprovalListVO> getApprovalList(String approverId, String type);

    /**
     * 获取审批详情
     */
    ApprovalDetailVO getApprovalDetail(Long id);

    /**
     * 处理审批
     */
    boolean processApproval(ApprovalProcessParam param);

    /**
     * 启动审批流程
     */
    void startApprovalProcess(Long leaveId);

    /**
     * 获取已审批记录
     */
    List<ApprovalRecordVO> getApprovalRecords(ApprovalRecordParam approvalRecordParam);

    /**
     * 取消审批
     */
    void cancelApprovalProcess(Long id);

    /**
     * 导出审批记录
     */
    byte[] exportApprovalRecords(ApprovalRecordParam approvalRecordParam);
}
