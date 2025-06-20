package com.snow.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.audit.entity.Leave;
import com.snow.audit.entity.param.LeaveApplyParam;
import com.snow.audit.entity.param.LeaveListParam;
import com.snow.audit.entity.vo.LeaveDetailVO;
import com.snow.audit.entity.vo.LeaveListVO;
import com.snow.audit.entity.vo.LeaveStatisticsVO;

import java.util.List;


public interface LeaveService extends IService<Leave> {
    /**
     * 提交请假申请
     */
    boolean applyLeave(LeaveApplyParam param);

    /**
     * 获取请假列表（分页）
     */
    IPage<LeaveListVO> getLeavePage(LeaveListParam param);

    /**
     * 获取请假详情
     */
    LeaveDetailVO getLeaveDetail(Long id);

    /**
     * 撤销请假申请
     */
    boolean cancelLeave(Long id, Long applicantId);

    /**
     * 审批请假申请
     */
    boolean approveLeave(Long id, Long approverId, String approverName,
                         String status, String comment);

    List<LeaveStatisticsVO> getLeaveStatistics(String applicantId, Integer year);

    /**
     * 获取请假列表（分页）
     */
    List<LeaveListVO> getLeaveList(LeaveListParam param);
}
