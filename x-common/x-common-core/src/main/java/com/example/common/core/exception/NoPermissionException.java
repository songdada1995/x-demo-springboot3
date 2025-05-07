package com.example.common.core.exception;

/**
 * 无权限异常
 */
public class NoPermissionException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public NoPermissionException(String message) {
        super(message);
    }

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
} 