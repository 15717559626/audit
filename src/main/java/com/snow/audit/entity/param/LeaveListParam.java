package com.snow.audit.entity.param;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author lish
 * @Date 2025/6/18 14:06
 * @DESCRIBE
 */
@Data
public class LeaveListParam {
    private Long applicantId;
    private Long approverId;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
