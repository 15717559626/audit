package com.snow.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.ApprovalConfig;
import com.snow.audit.entity.param.ApprovalConfigParam;
import com.snow.audit.entity.param.ApprovalProcessParam;
import com.snow.audit.entity.vo.*;

import java.util.List;

public interface ApprovalConfigService extends IService<ApprovalConfig> {

    ApprovalConfigVO getApprovalConfig();

    boolean saveApprovalConfig(ApprovalConfigParam param);

    boolean resetApprovalConfig();

    List<ApproverVO> getAvailableApprovers();

    boolean validateApprovalConfig(ApprovalConfigParam param);
}
