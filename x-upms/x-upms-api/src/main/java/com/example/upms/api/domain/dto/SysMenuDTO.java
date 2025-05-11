package com.example.upms.api.domain.dto;

import com.example.upms.api.domain.entity.SysMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysMenuDTO extends SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 子菜单 */
    private List<SysMenuDTO> children;
} 