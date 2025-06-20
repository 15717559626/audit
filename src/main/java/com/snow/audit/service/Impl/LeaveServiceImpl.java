package com.snow.audit.service.Impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.common.ApiException;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.Leave;
import com.snow.audit.entity.param.LeaveApplyParam;
import com.snow.audit.entity.param.LeaveListParam;
import com.snow.audit.entity.vo.LeaveDetailVO;
import com.snow.audit.entity.vo.LeaveListVO;
import com.snow.audit.entity.vo.LeaveStatisticsVO;
import com.snow.audit.mapper.LeaveMapper;
import com.snow.audit.service.ApprovalService;
import com.snow.audit.service.LeaveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements LeaveService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ApprovalService approvalService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean applyLeave(LeaveApplyParam param) {
        // 1. 参数校验
        validateLeaveApplication(param);

        // 2. 计算请假天数
        int days = calculateLeaveDays(param.getStartDate(), param.getEndDate());

        // 3. 构建请假记录
        Leave leave = new Leave();
        leave.setApplicantId(param.getApplicantId());
        leave.setApplicantName(param.getApplicantName());
        leave.setStartDate(param.getStartDate());
        leave.setEndDate(param.getEndDate());
        leave.setLeaveType(param.getLeaveType());
        leave.setLeaveTypeName(param.getLeaveTypeName());
        leave.setDays(days);
        leave.setReason(param.getReason());
        leave.setStatus("submitted"); // 改为submitted，等待进入审批流程
        leave.setCreateTime(LocalDateTime.now());
        leave.setUpdateTime(LocalDateTime.now());

        // 4. 保存到数据库
        boolean saveResult = save(leave);

        if (saveResult) {
            // 5. 启动审批流程
            try {
                approvalService.startApprovalProcess(leave.getId());
            } catch (Exception e) {
                // 如果审批流程启动失败，回滚请假申请
                removeById(leave.getId());
                throw new ApiException("启动审批流程失败：" + e.getMessage());
            }
        }

        return saveResult;
    }

    @Override
    public IPage<LeaveListVO> getLeavePage(LeaveListParam param) {
        // 构建分页对象
        Page<Leave> page = new Page<>(param.getPageNum(), param.getPageSize());

        // 构建查询条件
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();

        if (param.getApplicantId() != null) {
            queryWrapper.eq("applicant_id", param.getApplicantId());
        }

        if (StringUtils.isNotBlank(param.getStatus())) {
            queryWrapper.eq("status", param.getStatus());
        }

        if (param.getStartDate() != null) {
            queryWrapper.ge("start_date", param.getStartDate());
        }

        if (param.getEndDate() != null) {
            queryWrapper.le("end_date", param.getEndDate());
        }

        // 按创建时间倒序
        queryWrapper.orderByDesc("create_time");

        // 查询数据
        IPage<Leave> leavePages = page(page, queryWrapper);

        // 转换为VO对象
        IPage<LeaveListVO> result = new Page<>();
        result.setCurrent(leavePages.getCurrent());
        result.setSize(leavePages.getSize());
        result.setTotal(leavePages.getTotal());
        result.setPages(leavePages.getPages());

        List<LeaveListVO> voList = leavePages.getRecords().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());
        result.setRecords(voList);

        return result;
    }

    @Override
    public LeaveDetailVO getLeaveDetail(Long id) {
        Leave leave = getById(id);
        if (leave == null) {
            throw new ApiException("请假记录不存在");
        }

        return convertToDetailVO(leave);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelLeave(Long id, Long applicantId) {
        Leave leave = getById(id);
        if (leave == null) {
            throw new ApiException("请假记录不存在");
        }

        // 只有申请人才能撤销自己的申请
        if (!leave.getApplicantId().equals(applicantId)) {
            throw new ApiException("只能撤销自己的请假申请");
        }

        // 只有提交状态和审批中状态才能撤销
        if (!"submitted".equals(leave.getStatus()) && !"approving".equals(leave.getStatus())) {
            throw new ApiException("只有已提交或审批中的申请才能撤销");
        }

        // 更新请假状态
        leave.setStatus("cancelled");
        leave.setUpdateTime(LocalDateTime.now());
        boolean updateResult = updateById(leave);

        if (updateResult) {
            // 同时取消审批流程
            try {
                approvalService.cancelApprovalProcess(id);
            } catch (Exception e) {
                // 记录日志，但不影响撤销操作
                log.error("取消审批流程失败：{}");
            }
        }

        return updateResult;
    }

    @Override
    @Deprecated
    public boolean approveLeave(Long id, Long approverId, String approverName,
                                String status, String comment) {
        // 这个方法已废弃，审批操作现在通过ApprovalService处理
        throw new ApiException("请使用ApprovalService.processApproval方法进行审批操作");
    }

    @Override
    public List<LeaveListVO> getLeaveList(LeaveListParam param) {
        return this.lambdaQuery().eq(param.getApplicantId() != null, Leave::getApplicantId, param.getApplicantId())
                .eq(StringUtils.isNotEmpty(param.getStatus()), Leave::getStatus, param.getStatus())
                .ge(param.getStartDate() != null, Leave::getStartDate, param.getStartDate())
                .le(param.getEndDate() != null, Leave::getEndDate, param.getEndDate())
                .orderByDesc(Leave::getCreateTime).list().stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveStatisticsVO> getLeaveStatistics(String applicantId, Integer year) {
        // 获取指定年份的请假统计数据
        List<LeaveStatisticsVO> statistics = new ArrayList<>();

        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("applicant_id", applicantId)
                .eq("status", "approved") // 只统计已通过的请假
                .apply("YEAR(start_date) = {0}", year);

        List<Leave> approvedLeaves = this.list(queryWrapper);

        // 按请假类型分组统计
        Map<String, List<Leave>> typeMap = approvedLeaves.stream()
                .collect(Collectors.groupingBy(Leave::getLeaveType));

        for (Map.Entry<String, List<Leave>> entry : typeMap.entrySet()) {
            LeaveStatisticsVO stat = new LeaveStatisticsVO();
            stat.setLeaveType(entry.getKey());
            stat.setLeaveTypeName(entry.getValue().get(0).getLeaveTypeName());
            stat.setTotalDays(entry.getValue().stream()
                    .mapToInt(Leave::getDays)
                    .sum());
            stat.setTotalCount(entry.getValue().size());
            statistics.add(stat);
        }

        return statistics;
    }

    /**
     * 校验请假申请参数
     */
    private void validateLeaveApplication(LeaveApplyParam param) {
        // 基本参数校验
        if (param.getApplicantId() == null) {
            throw new ApiException("申请人ID不能为空");
        }
        if (StringUtils.isEmpty(param.getApplicantName())) {
            throw new ApiException("申请人姓名不能为空");
        }
        if (param.getStartDate() == null) {
            throw new ApiException("开始日期不能为空");
        }
        if (param.getEndDate() == null) {
            throw new ApiException("结束日期不能为空");
        }
        if (StringUtils.isEmpty(param.getLeaveType())) {
            throw new ApiException("请假类型不能为空");
        }
        if (StringUtils.isEmpty(param.getReason())) {
            throw new ApiException("请假原因不能为空");
        }

        // 日期校验
        if (param.getStartDate().isAfter(param.getEndDate())) {
            throw new ApiException("开始日期不能晚于结束日期");
        }

        // 不能申请过去的日期（当天可以申请）
        if (param.getStartDate().isBefore(LocalDate.now())) {
            throw new ApiException("不能申请过去的日期");
        }

        // 请假天数限制（可根据业务需求调整）
        int days = calculateLeaveDays(param.getStartDate(), param.getEndDate());
        if (days > 30) {
            throw new ApiException("单次请假不能超过30天");
        }

        // 检查是否有重叠的请假申请
        checkOverlappingLeave(param);
    }

    /**
     * 检查重叠的请假申请
     */
    private void checkOverlappingLeave(LeaveApplyParam param) {
        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("applicant_id", param.getApplicantId())
                .in("status", Arrays.asList("submitted", "approving", "approved"))
                .and(wrapper -> wrapper
                        // 新申请的开始日期在已有申请期间内
                        .between("start_date", param.getStartDate(), param.getEndDate())
                        .or()
                        // 新申请的结束日期在已有申请期间内
                        .between("end_date", param.getStartDate(), param.getEndDate())
                        .or()
                        // 新申请包含已有申请
                        .and(w -> w.le("start_date", param.getStartDate())
                                .ge("end_date", param.getEndDate()))
                );

        long count = count(queryWrapper);
        if (count > 0) {
            throw new ApiException("该时间段已有请假申请，请重新选择时间");
        }
    }

    /**
     * 计算请假天数（工作日）
     */
    private int calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        int days = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            // 排除周末（可根据业务需求调整）
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                days++;
            }
            current = current.plusDays(1);
        }

        return Math.max(days, 1); // 至少1天
    }

    /**
     * 转换为列表VO
     */
    private LeaveListVO convertToListVO(Leave leave) {
        LeaveListVO vo = new LeaveListVO();
        vo.setId(leave.getId());
        vo.setApplicantId(leave.getApplicantId());
        vo.setApplicantName(leave.getApplicantName());
        vo.setStartDate(leave.getStartDate());
        vo.setEndDate(leave.getEndDate());
        vo.setLeaveType(leave.getLeaveType());
        vo.setLeaveTypeName(leave.getLeaveTypeName());
        vo.setDays(leave.getDays());
        vo.setReason(leave.getReason());
        vo.setStatus(leave.getStatus());
        vo.setStatusName(getStatusName(leave.getStatus()));
        vo.setApplyTime(leave.getCreateTime().format(DATE_TIME_FORMATTER));
        vo.setApproverName(leave.getApproverName());
        return vo;
    }

    /**
     * 转换为详情VO
     */
    private LeaveDetailVO convertToDetailVO(Leave leave) {
        LeaveDetailVO vo = new LeaveDetailVO();
        vo.setId(leave.getId());
        vo.setApplicantId(leave.getApplicantId());
        vo.setApplicantName(leave.getApplicantName());
        vo.setStartDate(leave.getStartDate());
        vo.setEndDate(leave.getEndDate());
        vo.setLeaveType(leave.getLeaveType());
        vo.setLeaveTypeName(leave.getLeaveTypeName());
        vo.setDays(leave.getDays());
        vo.setReason(leave.getReason());
        vo.setStatus(leave.getStatus());
        vo.setStatusName(getStatusName(leave.getStatus()));
        vo.setApplyTime(leave.getCreateTime().format(DATE_TIME_FORMATTER));
        //获取审批人信息
        if (leave.getApproverId() == null){
            approvalService.lambdaQuery()
                    .eq(Approval::getLeaveId, leave.getId()).oneOpt().ifPresent(approval -> {
                        vo.setApproverId(approval.getCurrentApproverId());
                        vo.setApproverName(approval.getCurrentApproverName());
                        vo.setApproveTime(approval.getUpdateTime() != null ?
                                approval.getUpdateTime().format(DATE_TIME_FORMATTER) : null);
                    });
        }else{
            vo.setApproverId(leave.getApproverId());
            vo.setApproverName(leave.getApproverName());
            vo.setApproveTime(leave.getApproveTime() != null ?
                    leave.getApproveTime().format(DATE_TIME_FORMATTER) : null);
        }
        vo.setApproveComment(leave.getApproveComment());
        return vo;
    }

    /**
     * 获取状态显示名称
     */
    private String getStatusName(String status) {
        switch (status) {
            case "submitted": return "已提交";
            case "approving": return "审批中";
            case "approved": return "已通过";
            case "rejected": return "已拒绝";
            case "cancelled": return "已撤销";
            default: return "未知状态";
        }
    }
}
