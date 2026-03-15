package com.example.upms.api.aspect;

import com.example.common.core.annotation.Log;
import com.example.common.core.utils.IpLocationUtils;
import com.example.common.core.utils.ServletUtils;
import com.example.upms.api.domain.entity.SysOperLog;
import com.example.upms.api.service.AsyncOperLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 操作日志AOP切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final AsyncOperLogService asyncOperLogService;
    private final ObjectMapper objectMapper;

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            SysOperLog operLog = new SysOperLog();

            operLog.setStatus(e != null ? 1 : 0);

            if (e != null) {
                operLog.setErrorMsg(substring(e.getMessage(), 0, 2000));
            }

            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");

            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());

            String username = getUsernameFromRequest();
            operLog.setOperName(username);

            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            operLog.setOperIp(ServletUtils.getClientIP());
            operLog.setOperLocation(IpLocationUtils.resolveLocation(operLog.getOperIp()));

            operLog.setTitle(controllerLog.title());
            operLog.setBusinessType(controllerLog.businessType().getCode());
            operLog.setOperatorType(controllerLog.operatorType().getCode());

            if (controllerLog.isSaveRequestData()) {
                setRequestValue(joinPoint, operLog);
            }

            if (controllerLog.isSaveResponseData() && jsonResult != null) {
                operLog.setJsonResult(substring(objectMapper.writeValueAsString(jsonResult), 0, 2000));
            }

            operLog.setOperTime(LocalDateTime.now());

            asyncOperLogService.saveOperLogAsync(operLog);

        } catch (Exception exp) {
            log.error("操作日志记录异常: ", exp);
        }
    }

    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog) {
        try {
            Map<String, String[]> parameterMap = ServletUtils.getRequest().getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {
                String params = objectMapper.writeValueAsString(parameterMap);
                operLog.setOperParam(substring(params, 0, 2000));
            } else {
                Object[] args = joinPoint.getArgs();
                if (args != null && args.length > 0) {
                    String params = objectMapper.writeValueAsString(args);
                    operLog.setOperParam(substring(params, 0, 2000));
                }
            }
        } catch (Exception e) {
            log.warn("Failed to get request parameters", e);
        }
    }

    private String getUsernameFromRequest() {
        try {
            String username = ServletUtils.getRequest().getHeader("X-User-Name");
            if (username != null && !username.isEmpty()) {
                return username;
            }
            return "系统";
        } catch (Exception e) {
            return "系统";
        }
    }

    private String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        if (str.length() <= end) {
            return str;
        }
        return str.substring(start, end);
    }
}
