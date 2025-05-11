package com.example.upms.service.impl;

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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenuVO> list(SysMenuDTO menuDTO) {
        return List.of();
    }

    @Override
    public SysMenuVO getInfo(Long menuId) {
        return null;
    }

    @Override
    public void add(SysMenuDTO menuDTO) {

    }

    @Override
    public void edit(SysMenuDTO menuDTO) {

    }

    @Override
    public void remove(Long menuId) {

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
        return List.of();
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenuDTO menuDTO) {
        return List.of();
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return null;
    }

    @Override
    public void insertMenu(SysMenuDTO menuDTO) {

    }

    @Override
    public void updateMenu(SysMenuDTO menuDTO) {

    }

    @Override
    public void deleteMenuById(Long menuId) {

    }

    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        return Set.of();
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