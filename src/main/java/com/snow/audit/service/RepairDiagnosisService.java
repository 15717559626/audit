package com.snow.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.audit.entity.RepairDiagnosis;
import com.snow.audit.entity.param.RepairDiagnosisListParam;
import com.snow.audit.entity.param.RepairDiagnosisParam;
import com.snow.audit.entity.vo.RepairDiagnosisVO;

public interface RepairDiagnosisService extends IService<RepairDiagnosis> {

    /**
     * 新增维修诊断记录
     */
    boolean add(RepairDiagnosisParam param);

    /**
     * 更新维修诊断记录
     */
    boolean update(RepairDiagnosisParam param);

    /**
     * 删除维修诊断记录
     */
    boolean delete(Long id);

    /**
     * 根据ID查询详情
     */
    RepairDiagnosisVO getDetail(Long id);

    /**
     * 分页查询列表
     */
    IPage<RepairDiagnosisVO> getPage(RepairDiagnosisListParam param);
}
