package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.domain.dto.SysRoleDTO;
import com.example.admin.domain.entity.SysRole;
import com.example.admin.domain.vo.SysRoleVO;
import com.example.common.core.utils.PageUtils;

import java.util.List;
import java.util.Set;

public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查询角色列表
     */
    PageUtils<SysRoleVO> list(SysRoleDTO roleDTO);

    /**
     * 查询角色信息
     */
    SysRoleVO getInfo(Long roleId);

    /**
     * 新增角色
     */
    void add(SysRoleDTO roleDTO);

    /**
     * 修改角色
     */
    void edit(SysRoleDTO roleDTO);

    /**
     * 删除角色
     */
    void remove(Long roleId);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
    
    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 角色权限列表
     */
    Set<String> selectRoleKeysByUserId(Long userId);
    
    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleNameUnique(SysRole role);
    
    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean checkRoleKeyUnique(SysRole role);
    
    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean insertRole(SysRole role);
    
    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean updateRole(SysRole role);
    
    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    boolean updateRoleStatus(SysRole role);
    
    /**
     * 删除角色信息
     *
     * @param roleId 角色ID
     * @return 结果
     */
    boolean deleteRoleById(Long roleId);
    
    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    boolean deleteRoleByIds(Long[] roleIds);
} 