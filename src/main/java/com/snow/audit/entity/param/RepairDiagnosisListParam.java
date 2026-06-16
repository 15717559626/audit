package com.snow.audit.entity.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepairDiagnosisListParam {

    private String orderId;

    private String vehicle;

    private String dtc;

    private String resultVerification;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
