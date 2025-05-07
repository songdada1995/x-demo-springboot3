package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.domain.dto.SysRoleDTO;
import com.example.admin.domain.entity.SysRole;
import com.example.admin.domain.vo.SysRoleVO;
import com.example.admin.mapper.SysRoleMapper;
import com.example.admin.service.ISysRoleMenuService;
import com.example.admin.service.ISysRoleService;
import com.example.admin.service.ISysUserRoleService;
import com.example.common.core.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleMenuService roleMenuService;
    
    @Autowired
    private ISysUserRoleService userRoleService;

    @Override
    public PageUtils<SysRoleVO> list(SysRoleDTO roleDTO) {
        return null;
    }

    @Override
    public SysRoleVO getInfo(Long roleId) {
        return null;
    }

    @Override
    public void add(SysRoleDTO roleDTO) {

    }

    @Override
    public void edit(SysRoleDTO roleDTO) {

    }

    @Override
    public void remove(Long roleId) {

    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    public Set<String> selectRoleKeysByUserId(Long userId) {
        List<String> roles = baseMapper.selectRoleKeysByUserId(userId);
        return new HashSet<>(roles);
    }

    @Override
    public boolean checkRoleNameUnique(SysRole role) {
        Long roleId = role.getRoleId() == null ? -1L : role.getRoleId();
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName, role.getRoleName())
                .ne(roleId > 0, SysRole::getRoleId, roleId);
        return count(wrapper) == 0;
    }

    @Override
    public boolean checkRoleKeyUnique(SysRole role) {
        Long roleId = role.getRoleId() == null ? -1L : role.getRoleId();
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, role.getRoleKey())
                .ne(roleId > 0, SysRole::getRoleId, roleId);
        return count(wrapper) == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(SysRole role) {
        return save(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysRole role) {
        return updateById(role);
    }

    @Override
    public boolean updateRoleStatus(SysRole role) {
        return updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleById(Long roleId) {
        // 删除角色与菜单关联
        roleMenuService.deleteRoleMenuByRoleId(roleId);
        // 删除角色与用户关联
        userRoleService.deleteUserRoleByRoleId(roleId);
        return removeById(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleByIds(Long[] roleIds) {
        // 删除角色与菜单关联
        roleMenuService.deleteRoleMenuByRoleIds(roleIds);
        // 删除角色与用户关联
        for (Long roleId : roleIds) {
            userRoleService.deleteUserRoleByRoleId(roleId);
        }
        return removeByIds(Arrays.asList(roleIds));
    }
} 