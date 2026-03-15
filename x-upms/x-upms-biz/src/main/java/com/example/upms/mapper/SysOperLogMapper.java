package com.example.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upms.api.domain.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
