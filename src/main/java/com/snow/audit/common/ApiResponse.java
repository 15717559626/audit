package com.snow.audit.common;

/**
 * @Author lish
 * @Date 2025/6/17 16:49
 * @DESCRIBE
 */

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code; // 状态码
    private String message; // 消息
    private T data; // 数据

    public ApiResponse() {}

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "操作成功");
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data);
    }

    public static <T> ApiResponse<T> error() {
        return new ApiResponse<>(500, "操作失败");
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message);
    }
}
