package com.example.common.core.config;

import com.example.common.core.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Import;

/**
 * x-common-core 自动配置
 * 引入 Jackson、异步、全局异常处理等通用能力
 * 仅 Servlet 应用加载，Gateway(WebFlux) 不加载
 */
@ConditionalOnWebApplication(type = Type.SERVLET)
@Import({
        JacksonConfig.class,
        AsyncConfig.class,
        GlobalExceptionHandler.class
})
public class XCommonCoreAutoConfiguration {
}
