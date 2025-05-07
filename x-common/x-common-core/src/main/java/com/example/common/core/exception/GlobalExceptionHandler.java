package com.example.common.core.exception;

import com.example.common.core.response.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生业务异常.", request.getRequestURI(), e);
        return R.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler(NoPermissionException.class)
    public R<?> handleNoPermissionException(NoPermissionException e, HttpServletRequest request) {
        log.error("请求地址'{}',没有权限.", request.getRequestURI(), e);
        return R.fail(403, e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数校验失败.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<?> handleBindException(BindException e, HttpServletRequest request) {
        log.error("请求地址'{}',参数绑定失败.", request.getRequestURI(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(message);
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求地址'{}',不支持'{}'请求.", request.getRequestURI(), e.getMethod());
        return R.fail("不支持' " + e.getMethod() + "'请求");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return R.fail("服务器内部错误");
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生系统异常.", request.getRequestURI(), e);
        return R.fail("服务器内部错误");
    }
} 