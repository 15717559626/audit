package com.snow.audit.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lish
 * @Date 2025/6/19 14:38
 * @DESCRIBE
 */
@Data
public class ApproverVO implements Serializable {

    private Long id;
    private String name;
    private String department;
    private String position;
}
