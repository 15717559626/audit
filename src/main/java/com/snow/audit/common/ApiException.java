package com.snow.audit.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author lish
 * @Date 2025/6/17 17:18
 * @DESCRIBE
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends RuntimeException{
    private int statusCode; // 状态码
    private String message; // 错误消息
    private String details; // 详细的错误信息

    public ApiException() {}

    public ApiException(String message) {
        this.statusCode = -1;
        this.message = message;
    }

    public ApiException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ApiException(int statusCode, String message, String details) {
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

}
