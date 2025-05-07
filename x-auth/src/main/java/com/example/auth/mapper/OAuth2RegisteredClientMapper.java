package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auth.domain.OAuth2RegisteredClient;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OAuth2RegisteredClientMapper extends BaseMapper<OAuth2RegisteredClient> {
} 