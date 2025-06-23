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
    List<ApprovalListVO> getApprovalList(String approverId, String type);

    ApprovalDetailVO getApprovalDetail(Long id);

    boolean processApproval(ApprovalProcessParam param);

    boolean startApprovalProcess(Long leaveId);

    List<ApprovalRecordVO> getApprovalRecords(ApprovalRecordParam approvalRecordParam);

    void cancelApprovalProcess(Long id);

    byte[] exportApprovalRecords(ApprovalRecordParam approvalRecordParam);
}
