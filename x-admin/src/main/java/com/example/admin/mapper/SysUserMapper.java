package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 查询已分配用户角色的用户列表
     *
     * @param user 用户信息
     * @return 用户列表
     */
    List<SysUser> selectAllocatedList(@Param("user") SysUser user);
    
    /**
     * 查询未分配用户角色的用户列表
     *
     * @param user 用户信息
     * @return 用户列表
     */
    List<SysUser> selectUnallocatedList(@Param("user") SysUser user);
} 