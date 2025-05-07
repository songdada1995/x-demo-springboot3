package com.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.domain.entity.SysDict;
import com.example.admin.mapper.SysDictMapper;
import com.example.admin.service.ISysDictService;
import com.example.common.core.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(SysDict dict) {
        // 检查字典编码是否已存在
        if (getOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getCode, dict.getCode())
                .eq(SysDict::getDeleted, false)) != null) {
            throw new BusinessException("字典编码已存在");
        }

        dict.setCreateTime(LocalDateTime.now());
        dict.setDeleted(false);
        save(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(SysDict dict) {
        // 检查字典是否存在
        SysDict existingDict = getById(dict.getId());
        if (existingDict == null) {
            throw new BusinessException("字典不存在");
        }

        // 如果修改了字典编码，检查新编码是否已存在
        if (!existingDict.getCode().equals(dict.getCode())) {
            if (getOne(new LambdaQueryWrapper<SysDict>()
                    .eq(SysDict::getCode, dict.getCode())
                    .eq(SysDict::getDeleted, false)) != null) {
                throw new BusinessException("字典编码已存在");
            }
        }

        dict.setUpdateTime(LocalDateTime.now());
        updateById(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Long id) {
        // 检查字典是否存在
        SysDict dict = getById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }

        // 逻辑删除字典
        dict.setDeleted(true);
        updateById(dict);
    }
} 