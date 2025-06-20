package com.snow.audit.entity.param;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author lish
 * @Date 2025/6/20 11:32
 * @DESCRIBE
 */
@Data
public class ApprovalRecordParam {

    private String startDate;

    private String endDate;

    private String status;

    private String approverId;
}
