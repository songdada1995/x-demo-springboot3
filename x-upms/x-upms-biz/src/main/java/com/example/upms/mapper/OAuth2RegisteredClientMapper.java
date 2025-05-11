package com.example.upms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.upms.api.domain.entity.OAuth2RegisteredClient;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2RegisteredClientMapper extends BaseMapper<OAuth2RegisteredClient> {
} 