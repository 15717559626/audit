package com.snow.audit.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.common.ApiException;
import com.snow.audit.entity.RepairDiagnosis;
import com.snow.audit.entity.param.RepairDiagnosisListParam;
import com.snow.audit.entity.param.RepairDiagnosisParam;
import com.snow.audit.entity.vo.RepairDiagnosisVO;
import com.snow.audit.mapper.RepairDiagnosisMapper;
import com.snow.audit.service.RepairDiagnosisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairDiagnosisServiceImpl extends ServiceImpl<RepairDiagnosisMapper, RepairDiagnosis> implements RepairDiagnosisService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(RepairDiagnosisParam param) {
        validateOrderIdUnique(param.getOrderId(), null);

        RepairDiagnosis entity = new RepairDiagnosis();
        BeanUtils.copyProperties(param, entity);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        return save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(RepairDiagnosisParam param) {
        if (param.getId() == null) {
            throw new ApiException("ID不能为空");
        }

        RepairDiagnosis entity = getById(param.getId());
        if (entity == null) {
            throw new ApiException("维修诊断记录不存在");
        }

        validateOrderIdUnique(param.getOrderId(), param.getId());

        BeanUtils.copyProperties(param, entity);
        entity.setUpdateTime(LocalDateTime.now());

        return updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        if (id == null) {
            throw new ApiException("ID不能为空");
        }

        RepairDiagnosis entity = getById(id);
        if (entity == null) {
            throw new ApiException("维修诊断记录不存在");
        }

        return removeById(id);
    }

    @Override
    public RepairDiagnosisVO getDetail(Long id) {
        if (id == null) {
            throw new ApiException("ID不能为空");
        }

        RepairDiagnosis entity = getById(id);
        if (entity == null) {
            throw new ApiException("维修诊断记录不存在");
        }

        return convertToVO(entity);
    }

    @Override
    public IPage<RepairDiagnosisVO> getPage(RepairDiagnosisListParam param) {
        Page<RepairDiagnosis> page = new Page<>(param.getPageNum(), param.getPageSize());

        QueryWrapper<RepairDiagnosis> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(param.getOrderId())) {
            queryWrapper.like("order_id", param.getOrderId());
        }

        if (StringUtils.isNotBlank(param.getVehicle())) {
            queryWrapper.like("vehicle", param.getVehicle());
        }

        if (StringUtils.isNotBlank(param.getDtc())) {
            queryWrapper.eq("dtc", param.getDtc());
        }

        if (StringUtils.isNotBlank(param.getResultVerification())) {
            queryWrapper.eq("result_verification", param.getResultVerification());
        }

        if (param.getStartTime() != null) {
            queryWrapper.ge("create_time", param.getStartTime());
        }

        if (param.getEndTime() != null) {
            queryWrapper.le("create_time", param.getEndTime());
        }

        queryWrapper.orderByDesc("create_time");

        IPage<RepairDiagnosis> entityPage = page(page, queryWrapper);

        IPage<RepairDiagnosisVO> result = new Page<>();
        result.setCurrent(entityPage.getCurrent());
        result.setSize(entityPage.getSize());
        result.setTotal(entityPage.getTotal());
        result.setPages(entityPage.getPages());

        List<RepairDiagnosisVO> voList = entityPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        result.setRecords(voList);

        return result;
    }

    private void validateOrderIdUnique(String orderId, Long excludeId) {
        QueryWrapper<RepairDiagnosis> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        if (excludeId != null) {
            wrapper.ne("id", excludeId);
        }
        if (count(wrapper) > 0) {
            throw new ApiException("工单编号已存在：" + orderId);
        }
    }

    private RepairDiagnosisVO convertToVO(RepairDiagnosis entity) {
        RepairDiagnosisVO vo = new RepairDiagnosisVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
