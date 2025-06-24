package com.snow.audit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义API异常
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Result<Object>> handleApiException(ApiException ex) {
        log.error("API异常: {}", ex.getMessage(), ex);

        Result<Object> result = new Result<>();
        result.setCode(ex.getStatusCode() != -1 ? ex.getStatusCode() : 500);
        result.setMessage(ex.getMessage());
        result.setData(ex.getDetails());

        // 根据状态码返回对应的HTTP状态
        HttpStatus httpStatus = ex.getStatusCode() != -1 ?
                HttpStatus.valueOf(ex.getStatusCode()) : HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(httpStatus).body(result);
    }

    /**
     * 处理参数校验异常 (@RequestBody上validate失败后抛出的异常)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("参数校验异常: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Result<Map<String, String>> result = new Result<>();
        result.setCode(400);
        result.setMessage("参数校验失败");
        result.setData(errors);

        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理参数绑定异常 (@RequestParam上validate失败后抛出的异常)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Map<String, String>>> handleBindException(BindException ex) {
        log.error("参数绑定异常: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Result<Map<String, String>> result = new Result<>();
        result.setCode(400);
        result.setMessage("参数绑定失败");
        result.setData(errors);

        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理约束违反异常 (单个参数校验失败)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        log.error("约束违反异常: {}", ex.getMessage());

        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
        }

        Result<String> result = new Result<>();
        result.setCode(400);
        result.setMessage("参数校验失败");
        result.setData(sb.toString());

        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Result<Object>> handleNullPointerException(NullPointerException ex) {
        log.error("空指针异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("系统内部错误"));
    }

    /**
     * 处理IllegalArgumentException异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("参数异常: {}", ex.getMessage(), ex);

        Result<Object> result = new Result<>();
        result.setCode(400);
        result.setMessage(ex.getMessage());
        result.setData(null);

        return ResponseEntity.badRequest().body(result);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("运行时异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("系统运行异常: " + ex.getMessage()));
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Object>> handleException(Exception ex) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("系统异常，请联系管理员"));
    }
}
