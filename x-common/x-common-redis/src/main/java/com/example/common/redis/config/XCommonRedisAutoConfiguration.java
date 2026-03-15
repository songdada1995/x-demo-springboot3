package com.example.common.redis.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * x-common-redis 自动配置
 * 优先于 RedisAutoConfiguration，使用自定义 RedisTemplate 序列化
 */
@ConditionalOnClass(RedisConnectionFactory.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
@Import(RedisConfig.class)
public class XCommonRedisAutoConfiguration {
}
