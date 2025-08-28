package com.snow.audit.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.common.ApiException;
import com.snow.audit.entity.ApprovalConfig;
import com.snow.audit.entity.param.ApprovalConfigParam;
import com.snow.audit.entity.param.ApprovalStepParam;
import com.snow.audit.entity.param.ApproverParam;
import com.snow.audit.entity.vo.ApprovalConfigVO;
import com.snow.audit.entity.vo.ApprovalStepVO;
import com.snow.audit.entity.vo.ApproverVO;
import com.snow.audit.mapper.ApprovalConfigMapper;
import com.snow.audit.service.ApprovalConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class ApprovalConfigServiceImpl extends ServiceImpl<ApprovalConfigMapper, ApprovalConfig> implements ApprovalConfigService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ApprovalConfigVO getApprovalConfig() {
        List<ApprovalConfig> configList = this.lambdaQuery()
                .orderByAsc(ApprovalConfig::getStepOrder)
                .list();

        if (configList.isEmpty()) {
            // 返回默认配置
            return getDefaultApprovalConfig();
        }

        // 按步骤分组
        Map<Integer, List<ApprovalConfig>> stepMap = configList.stream()
                .collect(Collectors.groupingBy(ApprovalConfig::getStepOrder));

        List<ApprovalStepVO> steps = new ArrayList<>();
        for (Map.Entry<Integer, List<ApprovalConfig>> entry : stepMap.entrySet()) {
            ApprovalStepVO stepVO = new ApprovalStepVO();
            stepVO.setStepOrder(entry.getKey());

            List<ApprovalConfig> stepConfigs = entry.getValue();
            if (!stepConfigs.isEmpty()) {
                ApprovalConfig firstConfig = stepConfigs.get(0);
                stepVO.setName(firstConfig.getStepName());
                stepVO.setDescription(firstConfig.getDescription());
                stepVO.setApprovalType(firstConfig.getApprovalType());
            }

            // 设置审批人列表
            List<ApproverVO> approvers = stepConfigs.stream()
                    .map(config -> {
                        ApproverVO approver = new ApproverVO();
                        approver.setId(config.getApproverId());
                        approver.setName(config.getApproverName());
                        approver.setDepartment(config.getDepartment());
                        approver.setPosition(config.getPosition());
                        return approver;
                    })
                    .collect(Collectors.toList());

            stepVO.setApprovers(approvers);
            steps.add(stepVO);
        }

        // 按步骤顺序排序
        steps.sort(Comparator.comparing(ApprovalStepVO::getStepOrder));

        ApprovalConfigVO configVO = new ApprovalConfigVO();
        configVO.setSteps(steps);
        configVO.setEnabled(true);

        return configVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveApprovalConfig(ApprovalConfigParam param) {
        // 1. 参数验证
        validateApprovalConfigParam(param);

        // 2. 删除原有配置
        this.lambdaUpdate().remove();

        // 3. 保存新配置
        List<ApprovalConfig> configList = new ArrayList<>();

        for (int i = 0; i < param.getSteps().size(); i++) {
            ApprovalStepParam stepParam = param.getSteps().get(i);

            for (ApproverParam approverParam : stepParam.getApprovers()) {
                ApprovalConfig config = new ApprovalConfig();
                config.setStepOrder(i + 1);
                config.setStepName(stepParam.getName());
                config.setDescription(stepParam.getDescription());
                config.setApprovalType(stepParam.getApprovalType() != null ?
                        stepParam.getApprovalType() : "sequential"); // 默认顺序审批
                config.setApproverId(approverParam.getId());
                config.setApproverName(approverParam.getName());
                config.setDepartment(approverParam.getDepartment());
                config.setPosition(approverParam.getPosition());
                config.setCreateTime(LocalDateTime.now());
                config.setUpdateTime(LocalDateTime.now());

                configList.add(config);
            }
        }

        return this.saveBatch(configList);
    }

    @Override
    public boolean resetApprovalConfig() {
        // 1. 删除当前配置
        this.lambdaUpdate().remove();

        // 2. 设置默认配置
        ApprovalConfigParam defaultParam = createDefaultConfigParam();

        return saveApprovalConfig(defaultParam);
    }

    @Override
    public boolean validateApprovalConfig(ApprovalConfigParam param) {
        try {
            validateApprovalConfigParam(param);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取默认审批配置
     */
    private ApprovalConfigVO getDefaultApprovalConfig() {
        ApprovalConfigVO config = new ApprovalConfigVO();
        config.setEnabled(true);

        List<ApprovalStepVO> steps = new ArrayList<>();

        // 默认两级审批：部门经理 -> 人事总监
        ApprovalStepVO step1 = new ApprovalStepVO();
        step1.setStepOrder(1);
        step1.setName("部门经理审批");
        step1.setDescription("部门经理审核请假申请");
        step1.setApprovalType("sequential");

        List<ApproverVO> approvers1 = new ArrayList<>();
        ApproverVO approver1 = new ApproverVO();
        approver1.setId(9876543231L);
        approver1.setName("张经理");
        approver1.setDepartment("技术部");
        approver1.setPosition("部门经理");
        approvers1.add(approver1);
        step1.setApprovers(approvers1);

        ApprovalStepVO step2 = new ApprovalStepVO();
        step2.setStepOrder(2);
        step2.setName("人事审批");
        step2.setDescription("人事总监最终审核");
        step2.setApprovalType("sequential");

        List<ApproverVO> approvers2 = new ArrayList<>();
        ApproverVO approver2 = new ApproverVO();
        approver2.setId(123456789L);
        approver2.setName("李总监");
        approver2.setDepartment("人事部");
        approver2.setPosition("人事总监");
        approvers2.add(approver2);
        step2.setApprovers(approvers2);

        steps.add(step1);
        steps.add(step2);
        config.setSteps(steps);

        return config;
    }

    /**
     * 创建默认配置参数
     */
    private ApprovalConfigParam createDefaultConfigParam() {
        ApprovalConfigParam param = new ApprovalConfigParam();

        List<ApprovalStepParam> steps = new ArrayList<>();

        // 步骤1：部门经理审批
        ApprovalStepParam step1 = new ApprovalStepParam();
        step1.setName("部门经理审批");
        step1.setDescription("部门经理审核请假申请");
        step1.setApprovalType("sequential");

        List<ApproverParam> approvers1 = new ArrayList<>();
        ApproverParam approver1 = new ApproverParam();
        approver1.setId(123456789L);
        approver1.setName("张经理");
        approver1.setDepartment("技术部");
        approver1.setPosition("部门经理");
        approvers1.add(approver1);
        step1.setApprovers(approvers1);

        // 步骤2：人事审批
        ApprovalStepParam step2 = new ApprovalStepParam();
        step2.setName("人事审批");
        step2.setDescription("人事总监最终审核");
        step2.setApprovalType("sequential");

        List<ApproverParam> approvers2 = new ArrayList<>();
        ApproverParam approver2 = new ApproverParam();
        approver2.setId(123456729L);
        approver2.setName("李总监");
        approver2.setDepartment("人事部");
        approver2.setPosition("人事总监");
        approvers2.add(approver2);
        step2.setApprovers(approvers2);

        steps.add(step1);
        steps.add(step2);
        param.setSteps(steps);

        return param;
    }

    /**
     * 验证审批配置参数
     */
    private void validateApprovalConfigParam(ApprovalConfigParam param) {
        if (param == null) {
            throw new ApiException("审批配置参数不能为空");
        }

        if (param.getSteps() == null || param.getSteps().isEmpty()) {
            throw new ApiException("审批步骤不能为空");
        }

        if (param.getSteps().size() > 10) {
            throw new ApiException("审批步骤不能超过10个");
        }

        for (int i = 0; i < param.getSteps().size(); i++) {
            ApprovalStepParam step = param.getSteps().get(i);

            if (StringUtils.isEmpty(step.getName())) {
                throw new ApiException("第" + (i + 1) + "步审批名称不能为空");
            }

            if (step.getName().length() > 50) {
                throw new ApiException("第" + (i + 1) + "步审批名称不能超过50个字符");
            }

            if (step.getApprovers() == null || step.getApprovers().isEmpty()) {
                throw new ApiException("第" + (i + 1) + "步审批人不能为空");
            }

            if (step.getApprovers().size() > 5) {
                throw new ApiException("第" + (i + 1) + "步审批人不能超过5个");
            }

            // 验证审批人信息
            for (int j = 0; j < step.getApprovers().size(); j++) {
                ApproverParam approver = step.getApprovers().get(j);

                if (approver.getId() == null) {
                    throw new ApiException("第" + (i + 1) + "步第" + (j + 1) + "个审批人ID不能为空");
                }

                if (StringUtils.isEmpty(approver.getName())) {
                    throw new ApiException("第" + (i + 1) + "步第" + (j + 1) + "个审批人姓名不能为空");
                }
            }
        }
    }
}
