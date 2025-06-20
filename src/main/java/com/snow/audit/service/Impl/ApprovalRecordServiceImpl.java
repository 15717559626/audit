package com.snow.audit.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.entity.ApprovalRecord;
import com.snow.audit.entity.param.ApprovalRecordParam;
import com.snow.audit.entity.vo.ApprovalRecordVO;
import com.snow.audit.mapper.ApprovalRecordMapper;
import com.snow.audit.service.ApprovalRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalRecordServiceImpl extends ServiceImpl<ApprovalRecordMapper, ApprovalRecord>
        implements ApprovalRecordService {

    @Override
    public List<ApprovalRecord> getRecordsByLeaveId(Long leaveId) {
        return this.lambdaQuery()
                .eq(ApprovalRecord::getLeaveId, leaveId)
                .orderByAsc(ApprovalRecord::getStepOrder)
                .list();
    }

    @Override
    public List<ApprovalRecordVO> getRecordsByApproverId(ApprovalRecordParam approvalRecordParam) {
        return baseMapper.getRecordsByApproverId(approvalRecordParam);
    }
}
