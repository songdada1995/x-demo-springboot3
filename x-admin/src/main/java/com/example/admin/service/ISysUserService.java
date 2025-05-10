package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.domain.dto.SysUserDTO;
import com.example.admin.domain.entity.SysUser;
import com.example.admin.domain.vo.SysUserVO;
import com.example.common.core.utils.PageUtils;
import com.example.common.security.model.LoginUser;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {
    
    /**
     * 分页查询用户列表
     */
    PageUtils<SysUserVO> list(SysUserDTO userDTO);
    
    /**
     * 获取用户信息
     */
    SysUserVO getInfo(Long userId);
    
    /**
     * 新增用户
     */
    void add(SysUserDTO userDTO);
    
    /**
     * 修改用户
     */
    void edit(SysUserDTO userDTO);
    
    /**
     * 删除用户
     */
    void remove(Long userId);
    
    /**
     * 重置密码
     */
    void resetPwd(SysUserDTO userDTO);
    
    /**
     * 修改用户状态
     */
    void changeStatus(SysUserDTO userDTO);
    
    /**
     * 获取用户角色ID列表
     */
    List<Long> getRoleIds(Long userId);
    
    /**
     * 分配用户角色
     */
    void assignRole(SysUserDTO userDTO);
    
    /**
     * 根据条件分页查询用户列表
     */
    List<SysUser> selectUserList(SysUser user);
    
    /**
     * 根据条件分页查询已配用户角色列表
     */
    List<SysUser> selectAllocatedList(SysUser user);
    
    /**
     * 根据条件分页查询未分配用户角色列表
     */
    List<SysUser> selectUnallocatedList(SysUser user);
    
    /**
     * 通过用户名查询用户
     */
    SysUser selectUserByUserName(String userName);
    
    /**
     * 通过用户ID查询用户
     */
    SysUser selectUserById(Long userId);
    
    /**
     * 校验用户名称是否唯一
     */
    boolean checkUserNameUnique(SysUser user);
    
    /**
     * 校验手机号码是否唯一
     */
    boolean checkPhoneUnique(SysUser user);
    
    /**
     * 校验email是否唯一
     */
    boolean checkEmailUnique(SysUser user);
    
    /**
     * 新增用户信息
     */
    boolean insertUser(SysUser user);
    
    /**
     * 修改用户信息
     */
    boolean updateUser(SysUser user);
    
    /**
     * 修改用户状态
     */
    boolean updateUserStatus(SysUser user);
    
    /**
     * 修改用户基本信息
     */
    boolean updateUserProfile(SysUser user);
    
    /**
     * 重置用户密码
     */
    boolean resetPwd(SysUser user);
    
    /**
     * 删除用户信息
     */
    boolean deleteUserById(Long userId);
    
    /**
     * 批量删除用户信息
     */
    boolean deleteUserByIds(Long[] userIds);

    /**
     * 获取用户信息和权限信息
     *
     * @param username 用户名
     * @return 用户信息和权限信息
     */
    LoginUser getUserInfo(String username);
} 