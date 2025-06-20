package com.snow.audit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.audit.common.Result;
import com.snow.audit.entity.param.LeaveApplyParam;
import com.snow.audit.entity.param.LeaveListParam;
import com.snow.audit.entity.vo.LeaveDetailVO;
import com.snow.audit.entity.vo.LeaveListVO;
import com.snow.audit.service.LeaveService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/leave")
@Api(tags = "请假管理接口")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    /**
     * 提交请假申请
     */
    @PostMapping("/apply")
    public Result<Boolean> applyLeave(@RequestBody @Valid LeaveApplyParam param) {
        boolean success = leaveService.applyLeave(param);
        return Result.success(success);
    }

    /**
     * 获取请假列表
     */
    @GetMapping("/list")
    public Result<IPage<LeaveListVO>> getLeaveList(LeaveListParam param) {
        IPage<LeaveListVO> result = leaveService.getLeavePage(param);
        return Result.success(result);
    }

    /**
     * 获取请假详情
     */
    @GetMapping("/detail")
    public Result<LeaveDetailVO> getLeaveDetail(Long id) {
        LeaveDetailVO result = leaveService.getLeaveDetail(id);
        return Result.success(result);
    }

    /**
     * 撤销请假申请
     */
    @PostMapping("/cancel")
    public Result<Boolean> cancelLeave(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Long applicantId = Long.valueOf(params.get("applicantId").toString());
        boolean success = leaveService.cancelLeave(id, applicantId);
        return Result.success(success);
    }

    /**
     * 审批请假申请（管理员使用）
     */
    @PostMapping("/approve")
    public Result<Boolean> approveLeave(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Long approverId = Long.valueOf(params.get("approverId").toString());
        String approverName = params.get("approverName").toString();
        String status = params.get("status").toString();
        String comment = params.get("comment") != null ? params.get("comment").toString() : "";

        boolean success = leaveService.approveLeave(id, approverId, approverName, status, comment);
        return Result.success(success);
    }
}
