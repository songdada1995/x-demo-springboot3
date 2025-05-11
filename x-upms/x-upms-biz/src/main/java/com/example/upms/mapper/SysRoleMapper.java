package com.example.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upms.api.domain.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    
    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 角色权限列表
     */
    List<String> selectRoleKeysByUserId(@Param("userId") Long userId);
} 