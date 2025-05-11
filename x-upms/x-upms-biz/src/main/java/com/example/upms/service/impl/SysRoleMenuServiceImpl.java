package com.example.upms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upms.api.domain.entity.SysRoleMenu;
import com.example.upms.mapper.SysRoleMenuMapper;
import com.example.upms.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRoleMenu(Long roleId, List<Long> menuIds) {
        // 先删除原有关联
        deleteRoleMenuByRoleId(roleId);
        // 批量新增
        return baseMapper.batchInsert(roleId, menuIds) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleMenuByRoleId(Long roleId) {
        return baseMapper.deleteByRoleId(roleId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoleMenuByRoleIds(Long[] roleIds) {
        return Arrays.stream(roleIds).allMatch(this::deleteRoleMenuByRoleId);
    }

    @Override
    public boolean deleteRoleMenuByMenuId(Long menuId) {
        return false;
    }

    @Override
    public int selectCountRoleMenuByMenuId(Long menuId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getMenuId, menuId);
        return (int) count(wrapper);
    }
} 