package com.example.admin.domain.dto;

import com.example.admin.domain.entity.SysMenu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuDTO extends SysMenu {
    private static final long serialVersionUID = 1L;

    /** 子菜单 */
    private List<SysMenuDTO> children;
} 