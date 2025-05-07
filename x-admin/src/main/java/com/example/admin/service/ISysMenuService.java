package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.domain.dto.SysMenuDTO;
import com.example.admin.domain.entity.SysMenu;
import com.example.admin.domain.vo.SysMenuVO;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理核心服务接口
 */
public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 查询菜单列表
     */
    List<SysMenuVO> list(SysMenuDTO menuDTO);

    /**
     * 获取菜单详细信息
     */
    SysMenuVO getInfo(Long menuId);

    /**
     * 新增菜单
     */
    void add(SysMenuDTO menuDTO);

    /**
     * 修改菜单
     */
    void edit(SysMenuDTO menuDTO);

    /**
     * 删除菜单
     */
    void remove(Long menuId);

    /**
     * 构建前端所需要的菜单
     */
    List<SysMenuVO> buildMenuTree(List<SysMenuVO> menus);

    /**
     * 根据用户ID查询菜单
     */
    List<SysMenuVO> selectMenusByUserId(Long userId);

    /**
     * 查询菜单列表
     */
    List<SysMenu> selectMenuList(SysMenuDTO menuDTO);

    /**
     * 根据菜单ID查询菜单
     */
    SysMenu selectMenuById(Long menuId);

    /**
     * 新增菜单
     */
    void insertMenu(SysMenuDTO menuDTO);

    /**
     * 修改菜单
     */
    void updateMenu(SysMenuDTO menuDTO);

    /**
     * 删除菜单
     */
    void deleteMenuById(Long menuId);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectPermsByUserId(Long userId);
} 