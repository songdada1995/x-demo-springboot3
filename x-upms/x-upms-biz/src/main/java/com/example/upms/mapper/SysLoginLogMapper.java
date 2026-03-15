package com.example.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upms.api.domain.entity.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * 清空登录日志
     */
    void cleanLoginLog();
}
