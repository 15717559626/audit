package com.snow.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.audit.entity.ApprovalRecord;
import com.snow.audit.entity.param.ApprovalRecordParam;
import com.snow.audit.entity.vo.ApprovalRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 审批记录Mapper
 */
@Mapper
public interface ApprovalRecordMapper extends BaseMapper<ApprovalRecord> {

    List<ApprovalRecordVO> getRecordsByApprovalId(@Param("param") ApprovalRecordParam approvalRecordParam);
}
