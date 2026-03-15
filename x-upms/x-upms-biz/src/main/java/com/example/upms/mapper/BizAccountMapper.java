package com.example.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upms.api.domain.entity.BizAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BizAccountMapper extends BaseMapper<BizAccount> {
}
