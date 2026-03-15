package com.example.upms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upms.api.domain.dto.SysMenuDTO;
import com.example.upms.api.domain.entity.SysMenu;
import com.example.upms.api.domain.vo.SysMenuVO;
import com.example.upms.mapper.SysMenuMapper;
import com.example.upms.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenuVO> list(SysMenuDTO menuDTO) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(menuDTO.getMenuName() != null && !menuDTO.getMenuName().isEmpty(),
                        SysMenu::getMenuName, menuDTO.getMenuName())
                .eq(menuDTO.getMenuType() != null && !menuDTO.getMenuType().isEmpty(),
                        SysMenu::getMenuType, menuDTO.getMenuType())
                .eq(menuDTO.getPerms() != null && !menuDTO.getPerms().isEmpty(),
                        SysMenu::getPerms, menuDTO.getPerms())
                .orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menus = list(wrapper);
        return menus.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public SysMenuVO getInfo(Long menuId) {
        return convertToVO(getById(menuId));
    }

    @Override
    public void add(SysMenuDTO menuDTO) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuDTO, menu);
        save(menu);
    }

    @Override
    public void edit(SysMenuDTO menuDTO) {
        SysMenu menu = getById(menuDTO.getMenuId());
        if (menu != null) {
            BeanUtils.copyProperties(menuDTO, menu);
            updateById(menu);
        }
    }

    @Override
    public void remove(Long menuId) {
        removeById(menuId);
    }

    @Override
    public List<SysMenuVO> buildMenuTree(List<SysMenuVO> menus) {
        List<SysMenuVO> returnList = new ArrayList<>();
        List<Long> tempList = menus.stream().map(SysMenuVO::getMenuId).collect(Collectors.toList());
        for (SysMenuVO menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    @Override
    public List<SysMenuVO> selectMenusByUserId(Long userId) {
        List<SysMenu> menus = baseMapper.selectMenusByUserId(userId);
        List<SysMenuVO> result = menus.stream().map(this::convertToVO).collect(Collectors.toList());
        // 补充缺失的父级菜单，保证树形结构完整（如：用户仅有财务报表权限时，需补全 财务管理、业务平台）
        fillParentMenus(result);
        return result;
    }

    /**
     * 补充缺失的父级菜单，确保菜单树层级正确
     */
    private void fillParentMenus(List<SysMenuVO> menus) {
        Set<Long> menuIds = menus.stream().map(SysMenuVO::getMenuId).collect(Collectors.toSet());
        Set<Long> missingParentIds = new HashSet<>();
        for (SysMenuVO m : menus) {
            Long pid = m.getParentId();
            while (pid != null && pid != 0 && !menuIds.contains(pid)) {
                missingParentIds.add(pid);
                SysMenu parent = getById(pid);
                if (parent == null) break;
                pid = parent.getParentId();
            }
        }
        for (Long pid : missingParentIds) {
            SysMenu parent = getById(pid);
            if (parent != null && "0".equals(parent.getStatus())) {
                SysMenuVO vo = convertToVO(parent);
                menus.add(vo);
                menuIds.add(pid);
            }
        }
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenuDTO menuDTO) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(menuDTO.getMenuName() != null && !menuDTO.getMenuName().isEmpty(),
                        SysMenu::getMenuName, menuDTO.getMenuName())
                .orderByAsc(SysMenu::getOrderNum);
        return list(wrapper);
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return getById(menuId);
    }

    @Override
    public void insertMenu(SysMenuDTO menuDTO) {
        add(menuDTO);
    }

    @Override
    public void updateMenu(SysMenuDTO menuDTO) {
        edit(menuDTO);
    }

    @Override
    public void deleteMenuById(Long menuId) {
        removeById(menuId);
    }

    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        List<String> perms = baseMapper.selectPermsByUserId(userId);
        return new HashSet<>(perms);
    }

    private SysMenuVO convertToVO(SysMenu menu) {
        if (menu == null) {
            return null;
        }
        SysMenuVO vo = new SysMenuVO();
        BeanUtils.copyProperties(menu, vo);
        return vo;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenuVO> list, SysMenuVO t) {
        // 得到子节点列表
        List<SysMenuVO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuVO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuVO> getChildList(List<SysMenuVO> list, SysMenuVO t) {
        return list.stream().filter(n -> n.getParentId().equals(t.getMenuId())).collect(Collectors.toList());
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenuVO> list, SysMenuVO t) {
        return !getChildList(list, t).isEmpty();
    }
} 