package com.example.common.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.example.common.mybatis.handler.MyMetaObjectHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

/**
 * x-common-mybatis 自动配置
 */
@ConditionalOnClass(MybatisPlusInterceptor.class)
@Import({
        MybatisPlusConfig.class,
        MyMetaObjectHandler.class
})
public class XCommonMybatisAutoConfiguration {
}
