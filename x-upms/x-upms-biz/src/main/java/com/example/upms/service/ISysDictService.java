package com.example.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upms.api.domain.entity.SysDict;

public interface ISysDictService extends IService<SysDict> {
    void addDict(SysDict dict);

    void updateDict(SysDict dict);

    void deleteDict(Long id);
} 