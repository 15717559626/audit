package com.snow.audit.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.common.ApiException;
import com.snow.audit.common.CustomStyleHandler;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.ApprovalRecord;
import com.snow.audit.entity.Leave;
import com.snow.audit.entity.enums.ApprovalStatusEnum;
import com.snow.audit.entity.enums.LeaveStatusEnum;
import com.snow.audit.entity.param.ApprovalProcessParam;
import com.snow.audit.entity.param.ApprovalRecordParam;
import com.snow.audit.entity.vo.*;
import com.snow.audit.mapper.ApprovalMapper;
import com.snow.audit.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.snow.audit.common.AuditConstant.auditApplyTemplateId;
import static com.snow.audit.common.AuditConstant.auditResultTemplateId;

@Service
@Transactional
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, Approval> implements ApprovalService {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private ApprovalConfigService approvalConfigService;

    @Autowired
    private ApprovalRecordService approvalRecordService;

    @Autowired
    private IBuUserService userService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ApprovalListVO> getApprovalList(String approverId, String type) {
        // 1. 查询当前用户待审批的申请
        return this.lambdaQuery().eq(Approval::getStatus, LeaveStatusEnum.APPROVING)
                .eq(Approval::getCurrentApproverId, approverId)
                .eq(StringUtils.isNotBlank(type), Approval::getType, type)
                .orderByDesc(Approval::getCreateTime).list().stream()
                .map(this::convertToApprovalListVO)
                .collect(Collectors.toList());
    }

    @Override
    public ApprovalDetailVO getApprovalDetail(Long id) {
        // 1. 获取审批记录
        Approval approval = this.getById(id);
        if (approval == null) {
            throw new ApiException("审批记录不存在");
        }

        // 2. 获取请假详情
        Leave leave = leaveService.getById(approval.getLeaveId());
        if (leave == null) {
            throw new ApiException("请假申请不存在");
        }

        // 3. 获取审批历史记录
        List<ApprovalRecord> records = approvalRecordService.lambdaQuery()
                .eq(ApprovalRecord::getLeaveId, approval.getLeaveId())
                .orderByAsc(ApprovalRecord::getStepOrder)
                .list();

        // 4. 转换为VO
        ApprovalDetailVO detail = new ApprovalDetailVO();

        // 基本信息
        detail.setId(approval.getId());
        detail.setLeaveId(approval.getLeaveId());
        detail.setType(approval.getType());
        detail.setStatus(approval.getStatus());
        detail.setCurrentStep(approval.getCurrentStep());
        detail.setTotalSteps(approval.getTotalSteps());

        // 请假申请信息
        detail.setApplicantId(leave.getApplicantId());
        detail.setApplicantName(leave.getApplicantName());
        detail.setStartDate(leave.getStartDate());
        detail.setEndDate(leave.getEndDate());
        detail.setLeaveType(leave.getLeaveType());
        detail.setLeaveTypeName(leave.getLeaveTypeName());
        detail.setDays(leave.getDays());
        detail.setReason(leave.getReason());
        detail.setApplyTime(leave.getCreateTime());
        detail.setDepartment(leave.getDepartment());

        // 审批流程记录
        List<ApprovalStepVO> stepList = records.stream()
                .map(this::convertToApprovalStepVO)
                .collect(Collectors.toList());
        detail.setApprovalSteps(stepList);

        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processApproval(ApprovalProcessParam param) {
        // 1. 参数验证
        validateApprovalProcessParam(param);

        // 2. 获取审批记录
        Approval approval = this.getById(param.getId());
        if (approval == null) {
            throw new ApiException("审批记录不存在");
        }

        // 3. 状态检查
        if (!ApprovalStatusEnum.APPROVING.getCode().equals(approval.getStatus())) {
            throw new ApiException("该申请已处理，无法重复审批");
        }

        // 4. 权限检查
        if (!approval.getCurrentApproverId().equals(param.getApproverId())) {
            throw new ApiException("您没有权限审批此申请");
        }

        // 5. 记录当前步骤的审批结果
        ApprovalRecord record = new ApprovalRecord();
        record.setLeaveId(approval.getLeaveId());
        record.setStepOrder(approval.getCurrentStep());
        record.setApproverId(param.getApproverId());
        record.setApproverName(param.getApproverName());
        record.setResult(param.getStatus());
        record.setComment(param.getComment());
        record.setApproveTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        approvalRecordService.save(record);

        // 6. 处理审批结果
        if (ApprovalStatusEnum.REJECTED.getCode().equals(param.getStatus())) {
            // 拒绝：直接结束流程
            return handleApprovalRejection(approval, param, record);
        } else if (ApprovalStatusEnum.APPROVED.getCode().equals(param.getStatus())) {
            // 通过：检查是否还有下一步
            return handleApprovalApprove(approval, param, record);
        } else {
            throw new ApiException("无效的审批状态");
        }
    }

    @Override
    public List<ApprovalRecordVO> getApprovalRecords(ApprovalRecordParam approvalRecordParam) {
        return approvalRecordService.getRecordsByApproverId(approvalRecordParam);
    }

    @Override
    public void cancelApprovalProcess(Long id) {
        //删除审批记录表中的记录
        approvalRecordService.lambdaUpdate().eq(ApprovalRecord::getLeaveId, id).remove();
        //删除审批表中的记录
        this.lambdaUpdate().eq(Approval::getLeaveId, id).remove();
    }

    @Override
    public byte[] exportApprovalRecords(ApprovalRecordParam approvalRecordParam) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 根据参数获取数据
            List<ApprovalRecordVO> records = getApprovalRecords(approvalRecordParam);

            // 转换为导出VO
            List<ApprovalRecordExportVO> exportData = records.stream()
                    .map(ApprovalRecordExportVO::new)
                    .collect(Collectors.toList());

            // 自定义样式写入Excel
            EasyExcel.write(outputStream, ApprovalRecordExportVO.class)
                    //.head(createCustomHead()) // 可选：自定义表头
                    .registerWriteHandler(new CustomStyleHandler()) // 可选：自定义样式
                    .sheet("审批记录")
                    .doWrite(exportData);

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startApprovalProcess(Long leaveId) {
        // 1. 获取请假申请
        Leave leave = leaveService.getById(leaveId);
        if (leave == null) {
            throw new ApiException("请假申请不存在");
        }

        // 2. 获取审批配置
        ApprovalConfigVO config = approvalConfigService.getApprovalConfig();
        if (config == null || config.getSteps().isEmpty()) {
            throw new ApiException("审批流程配置不存在，请联系管理员");
        }

        // 3. 创建审批流程
        Approval approval = new Approval();
        approval.setLeaveId(leaveId);
        approval.setType("leave"); // 请假类型
        approval.setStatus(ApprovalStatusEnum.APPROVING.getCode());
        approval.setCurrentStep(1);
        approval.setTotalSteps(config.getSteps().size());
        approval.setCurrentApproverId(config.getSteps().get(0).getApprovers().get(0).getId());
        approval.setCurrentApproverName(config.getSteps().get(0).getApprovers().get(0).getName());
        approval.setCreateTime(LocalDateTime.now());
        approval.setUpdateTime(LocalDateTime.now());

        // 4. 保存审批记录
        this.save(approval);

        // 5. 更新请假申请状态
        leave.setStatus(LeaveStatusEnum.APPROVING.getCode());
        leave.setUpdateTime(LocalDateTime.now());
        leaveService.updateById(leave);

        // 6. 发送待审批订阅消息
        userService.sendSubscribeMessage(leave.getApplicantId(), auditApplyTemplateId, "pages/approval/detail?id=" + approval.getId(), leave);

        return true;
    }

    /**
     * 处理审批拒绝
     */
    private boolean handleApprovalRejection(Approval approval, ApprovalProcessParam param, ApprovalRecord record) {
        // 更新审批状态
        approval.setStatus("rejected");
        approval.setUpdateTime(LocalDateTime.now());
        this.updateById(approval);

        // 更新请假申请状态
        Leave leave = leaveService.getById(approval.getLeaveId());
        leave.setStatus("rejected");
        leave.setApproverId(param.getApproverId());
        leave.setApproverName(param.getApproverName());
        leave.setApproveTime(LocalDateTime.now());
        leave.setApproveComment(param.getComment());
        leave.setUpdateTime(LocalDateTime.now());
        leaveService.updateById(leave);

        // 审批通过后发送订阅消息
        userService.sendSubscribeMessage(leave.getApplicantId(), auditResultTemplateId, "pages/leave/detail?id=" + leave.getId(), record);

        return true;
    }

    /**
     * 处理审批通过
     */
    private boolean handleApprovalApprove(Approval approval, ApprovalProcessParam param, ApprovalRecord record) {
        // 检查是否是最后一步
        if (approval.getCurrentStep().equals(approval.getTotalSteps())) {
            // 最后一步，审批完成
            approval.setStatus("approved");
            approval.setUpdateTime(LocalDateTime.now());
            this.updateById(approval);

            // 更新请假申请状态
            Leave leave = leaveService.getById(approval.getLeaveId());
            leave.setStatus("approved");
            leave.setApproverId(param.getApproverId());
            leave.setApproverName(param.getApproverName());
            leave.setApproveTime(LocalDateTime.now());
            leave.setApproveComment(param.getComment());
            leave.setUpdateTime(LocalDateTime.now());
            leaveService.updateById(leave);

            // 审批通过后发送订阅消息
            userService.sendSubscribeMessage(leave.getApplicantId(), auditResultTemplateId, "pages/leave/detail?id=", record);
        } else {
            // 进入下一步审批
            ApprovalConfigVO config = approvalConfigService.getApprovalConfig();
            int nextStep = approval.getCurrentStep() + 1;
            ApprovalStepVO nextStepConfig = config.getSteps().get(nextStep - 1);

            approval.setCurrentStep(nextStep);
            approval.setCurrentApproverId(nextStepConfig.getApprovers().get(0).getId());
            approval.setCurrentApproverName(nextStepConfig.getApprovers().get(0).getName());
            approval.setUpdateTime(LocalDateTime.now());
            this.updateById(approval);
        }

        return true;
    }

    /**
     * 参数验证
     */
    private void validateApprovalProcessParam(ApprovalProcessParam param) {
        if (param.getId() == null) {
            throw new ApiException("审批ID不能为空");
        }
        if (param.getApproverId() == null) {
            throw new ApiException("审批人ID不能为空");
        }
        if (StringUtils.isEmpty(param.getApproverName())) {
            throw new ApiException("审批人姓名不能为空");
        }
        if (StringUtils.isEmpty(param.getStatus())) {
            throw new ApiException("审批状态不能为空");
        }
        if (!"approved".equals(param.getStatus()) && !"rejected".equals(param.getStatus())) {
            throw new ApiException("审批状态必须是approved或rejected");
        }
    }

    /**
     * 转换为审批列表VO
     */
    private ApprovalListVO convertToApprovalListVO(Approval approval) {
        ApprovalListVO vo = new ApprovalListVO();
        vo.setId(approval.getId());
        vo.setLeaveId(approval.getLeaveId());
        vo.setType(approval.getType());
        vo.setStatus(approval.getStatus());
        vo.setCurrentStep(approval.getCurrentStep());
        vo.setTotalSteps(approval.getTotalSteps());
        vo.setCreateTime(approval.getCreateTime());

        // 获取请假详情
        Leave leave = leaveService.getById(approval.getLeaveId());
        if (leave != null) {
            vo.setApplicantName(leave.getApplicantName());
            vo.setDepartment(leave.getDepartment());
            vo.setLeaveTypeName(leave.getLeaveTypeName());
            vo.setStartDate(leave.getStartDate());
            vo.setEndDate(leave.getEndDate());
            vo.setDays(leave.getDays());
            vo.setReason(leave.getReason());
            vo.setApplyTime(leave.getCreateTime());
        }

        return vo;
    }

    /**
     * 转换为审批步骤VO
     */
    private ApprovalStepVO convertToApprovalStepVO(ApprovalRecord record) {
        ApprovalStepVO vo = new ApprovalStepVO();
        vo.setStepOrder(record.getStepOrder());
        vo.setApproverId(record.getApproverId());
        vo.setApproverName(record.getApproverName());
        vo.setResult(record.getResult());
        vo.setComment(record.getComment());
        vo.setApproveTime( record.getApproveTime());
        return vo;
    }

    /**
     * 转换为审批记录VO
     */
    private ApprovalRecordVO convertToApprovalRecordVO(ApprovalRecord record) {
        ApprovalRecordVO vo = new ApprovalRecordVO();
        vo.setId(record.getId());
        vo.setLeaveId(record.getLeaveId());
        vo.setApproverId(record.getApproverId());
        vo.setApproverName(record.getApproverName());
        vo.setResult(record.getResult());
        vo.setComment(record.getComment());
        vo.setApproveTime( record.getApproveTime());
        vo.setCreateTime(record.getCreateTime().format(DATE_TIME_FORMATTER));
        return vo;
    }

}
