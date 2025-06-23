package com.snow.audit.controller;

import com.snow.audit.common.Result;
import com.snow.audit.entity.param.ApprovalConfigParam;
import com.snow.audit.entity.param.ApprovalProcessParam;
import com.snow.audit.entity.param.ApprovalRecordParam;
import com.snow.audit.entity.vo.ApprovalConfigVO;
import com.snow.audit.entity.vo.ApprovalDetailVO;
import com.snow.audit.entity.vo.ApprovalListVO;
import com.snow.audit.entity.vo.ApprovalRecordVO;
import com.snow.audit.service.ApprovalConfigService;
import com.snow.audit.service.ApprovalService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approval")
@Api(tags = "审批管理接口")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ApprovalConfigService approvalConfigService;

    // 获取待审批列表
    @GetMapping("/list")
    public Result<List<ApprovalListVO>> getApprovalList(
            @RequestParam(required = false) String approverId,
            @RequestParam(required = false) String type) {
        List<ApprovalListVO> list = approvalService.getApprovalList(approverId, type);
        return Result.success(list);
    }

    // 获取审批详情
    @GetMapping("/detail/{id}")
    public Result<ApprovalDetailVO> getApprovalDetail(@PathVariable Long id) {
        ApprovalDetailVO detail = approvalService.getApprovalDetail(id);
        return Result.success(detail);
    }

    // 处理审批
    @PostMapping("/process")
    public Result<Boolean> processApproval(@RequestBody ApprovalProcessParam param) {
        boolean success = approvalService.processApproval(param);
        return Result.success(success);
    }

    @PostMapping("/records")
    public Result<List<ApprovalRecordVO>> processApproval(@RequestBody ApprovalRecordParam approvalRecordParam) {
        List<ApprovalRecordVO> approvalRecords = approvalService.getApprovalRecords(approvalRecordParam);
        return Result.success(approvalRecords);
    }

    @PostMapping("/export")
    public Result<byte[]> exportApproval(@RequestBody ApprovalRecordParam approvalRecordParam) {
        return Result.success(approvalService.exportApprovalRecords(approvalRecordParam));
    }

    // 获取审批流配置
    @GetMapping("/config")
    public Result<ApprovalConfigVO> getApprovalConfig() {
        ApprovalConfigVO config = approvalConfigService.getApprovalConfig();
        return Result.success(config);
    }

    // 保存审批流配置
    @PostMapping("/config")
    public Result<Boolean> saveApprovalConfig(@RequestBody ApprovalConfigParam param) {
        boolean success = approvalConfigService.saveApprovalConfig(param);
        return Result.success(success);
    }
}
