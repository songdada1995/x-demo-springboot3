package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.domain.entity.SysUserRole;

import java.util.List;

public interface ISysUserRoleService extends IService<SysUserRole> {
    
    /**
     * 批量新增用户角色关联信息
     *
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 结果
     */
    boolean insertUserRole(Long userId, List<Long> roleIds);
    
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    boolean deleteUserRoleByUserId(Long userId);
    
    /**
     * 通过角色ID删除用户和角色关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    boolean deleteUserRoleByRoleId(Long roleId);
    
    /**
     * 批量删除用户角色关联信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    boolean deleteUserRoleByUserIds(Long[] userIds);
    
    /**
     * 查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    int selectCountUserRoleByRoleId(Long roleId);
} 