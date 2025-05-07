package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.domain.entity.SysDict;

public interface ISysDictService extends IService<SysDict> {
    void addDict(SysDict dict);
    void updateDict(SysDict dict);
    void deleteDict(Long id);
} 