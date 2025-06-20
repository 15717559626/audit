package com.snow.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.ApprovalConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApprovalConfigMapper extends BaseMapper<ApprovalConfig> {
}
