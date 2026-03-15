package com.example.upms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.core.exception.BusinessException;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.dto.SysRoleDTO;
import com.example.upms.api.domain.entity.SysRole;
import com.example.upms.api.domain.vo.SysRoleVO;
import com.example.upms.mapper.SysRoleMapper;
import com.example.upms.service.ISysRoleMenuService;
import com.example.upms.service.ISysRoleService;
import com.example.upms.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysRoleMenuService roleMenuService;
    
    @Autowired
    private ISysUserRoleService userRoleService;

    @Override
    public PageUtils<SysRoleVO> list(SysRoleDTO roleDTO) {
        int pageNum = roleDTO.getPageNum() != null ? roleDTO.getPageNum() : 1;
        int pageSize = roleDTO.getPageSize() != null ? roleDTO.getPageSize() : 10;
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<SysRole> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(roleDTO.getRoleName() != null && !roleDTO.getRoleName().isEmpty(),
                        SysRole::getRoleName, roleDTO.getRoleName())
                .like(roleDTO.getRoleKey() != null && !roleDTO.getRoleKey().isEmpty(),
                        SysRole::getRoleKey, roleDTO.getRoleKey())
                .eq(roleDTO.getStatus() != null, SysRole::getStatus, roleDTO.getStatus())
                .orderByAsc(SysRole::getRoleSort);

        page(page, wrapper);

        List<SysRoleVO> voList = page.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        return new PageUtils<>(voList, page.getTotal(), page.getSize(), page.getCurrent());
    }

    @Override
    public SysRoleVO getInfo(Long roleId) {
        SysRole role = getById(roleId);
        return convertToVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysRoleDTO roleDTO) {
        SysRole checkName = new SysRole();
        checkName.setRoleName(roleDTO.getRoleName());
        if (!checkRoleNameUnique(checkName)) {
            throw new BusinessException("角色名称已存在");
        }
        if (roleDTO.getRoleKey() != null && !roleDTO.getRoleKey().isEmpty()) {
            SysRole checkKey = new SysRole();
            checkKey.setRoleKey(roleDTO.getRoleKey());
            if (!checkRoleKeyUnique(checkKey)) {
                throw new BusinessException("角色编码已存在");
            }
        }
        SysRole role = new SysRole();
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setRoleSort(roleDTO.getRoleSort() != null ? roleDTO.getRoleSort() : 0);
        role.setStatus(roleDTO.getStatus() != null ? roleDTO.getStatus() : 0);
        role.setRemark(roleDTO.getRemark());
        save(role);

        if (role.getRoleKey() == null || role.getRoleKey().isEmpty()) {
            role.setRoleKey("role_" + role.getRoleId());
            updateById(role);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysRoleDTO roleDTO) {
        SysRole role = getById(roleDTO.getRoleId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        SysRole checkName = new SysRole();
        checkName.setRoleId(roleDTO.getRoleId());
        checkName.setRoleName(roleDTO.getRoleName());
        if (!checkRoleNameUnique(checkName)) {
            throw new BusinessException("角色名称已存在");
        }
        if (roleDTO.getRoleKey() != null && !roleDTO.getRoleKey().isEmpty()) {
            SysRole checkKey = new SysRole();
            checkKey.setRoleId(roleDTO.getRoleId());
            checkKey.setRoleKey(roleDTO.getRoleKey());
            if (!checkRoleKeyUnique(checkKey)) {
                throw new BusinessException("角色编码已存在");
            }
        }
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleKey(roleDTO.getRoleKey());
        role.setRoleSort(roleDTO.getRoleSort());
        role.setStatus(roleDTO.getStatus());
        role.setRemark(roleDTO.getRemark());
        updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long roleId) {
        if (roleId != null && roleId == 1L) {
            throw new BusinessException("不允许删除超级管理员角色");
        }
        deleteRoleById(roleId);
    }

    private SysRoleVO convertToVO(SysRole role) {
        if (role == null) return null;
        SysRoleVO vo = new SysRoleVO();
        vo.setRoleId(role.getRoleId());
        vo.setRoleName(role.getRoleName());
        vo.setRoleKey(role.getRoleKey());
        vo.setRoleSort(role.getRoleSort());
        vo.setDataScope(role.getDataScope());
        vo.setStatus(role.getStatus());
        vo.setRemark(role.getRemark());
        vo.setCreateTime(role.getCreateTime());
        return vo;
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