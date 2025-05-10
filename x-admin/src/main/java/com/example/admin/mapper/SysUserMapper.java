package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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

    /**
     * 查询用户权限数据
     *
     * @param userId
     * @return
     */
    Set<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 查询用户角色数据
     *
     * @param userId
     * @return
     */
    Set<String> selectRolesByUserId(@Param("userId") Long userId);
}