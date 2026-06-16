package com.snow.audit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.snow.audit.common.Result;
import com.snow.audit.entity.param.RepairDiagnosisListParam;
import com.snow.audit.entity.param.RepairDiagnosisParam;
import com.snow.audit.entity.vo.RepairDiagnosisVO;
import com.snow.audit.service.RepairDiagnosisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/repair-diagnosis")
@Api(tags = "维修诊断知识库接口")
public class RepairDiagnosisController {

    @Autowired
    private RepairDiagnosisService repairDiagnosisService;

    @ApiOperation("新增维修诊断记录")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody @Valid RepairDiagnosisParam param) {
        boolean success = repairDiagnosisService.add(param);
        return Result.success(success);
    }

    @ApiOperation("更新维修诊断记录")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid RepairDiagnosisParam param) {
        boolean success = repairDiagnosisService.update(param);
        return Result.success(success);
    }

    @ApiOperation("删除维修诊断记录")
    @GetMapping("/delete")
    public Result<Boolean> delete(Long id) {
        boolean success = repairDiagnosisService.delete(id);
        return Result.success(success);
    }

    @ApiOperation("查询维修诊断详情")
    @GetMapping("/detail")
    public Result<RepairDiagnosisVO> detail(Long id) {
        RepairDiagnosisVO result = repairDiagnosisService.getDetail(id);
        return Result.success(result);
    }

    @ApiOperation("分页查询维修诊断列表")
    @GetMapping("/list")
    public Result<IPage<RepairDiagnosisVO>> list(RepairDiagnosisListParam param) {
        IPage<RepairDiagnosisVO> result = repairDiagnosisService.getPage(param);
        return Result.success(result);
    }
}
