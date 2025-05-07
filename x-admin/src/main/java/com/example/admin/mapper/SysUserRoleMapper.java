package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    
    /**
     * 批量新增用户角色关联信息
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 结果
     */
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 通过角色ID删除用户和角色关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
} 