package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单管理数据层
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询菜单
     */
    List<SysMenu> selectMenusByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据用户ID查询权限
     */
    List<String> selectPermsByUserId(@Param("userId") Long userId);
} 