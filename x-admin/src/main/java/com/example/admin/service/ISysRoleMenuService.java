package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.domain.entity.SysRoleMenu;

import java.util.List;

public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 批量新增角色菜单关联信息
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 结果
     */
    boolean insertRoleMenu(Long roleId, List<Long> menuIds);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    boolean deleteRoleMenuByRoleId(Long roleId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleIds 角色ID
     * @return 结果
     */
    boolean deleteRoleMenuByRoleIds(Long[] roleIds);

    /**
     * 通过菜单ID删除角色和菜单关联
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    boolean deleteRoleMenuByMenuId(Long menuId);

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int selectCountRoleMenuByMenuId(Long menuId);
} 