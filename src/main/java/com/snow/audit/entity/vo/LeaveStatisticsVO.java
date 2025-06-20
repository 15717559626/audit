package com.snow.audit.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 请假统计VO
 */
@Data
public class LeaveStatisticsVO implements Serializable {

    private String leaveType;
    private String leaveTypeName;
    private Integer totalDays;
    private Integer totalCount;
}
