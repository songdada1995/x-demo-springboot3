package com.example.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.domain.dto.SysUserDTO;
import com.example.admin.domain.entity.*;
import com.example.admin.domain.vo.SysUserVO;
import com.example.admin.mapper.SysUserMapper;
import com.example.admin.mapper.SysUserRoleMapper;
import com.example.admin.service.ISysUserRoleService;
import com.example.admin.service.ISysUserService;
import com.example.common.core.utils.PageUtils;
import com.example.common.core.utils.StringUtils;
import com.example.common.security.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final ISysUserRoleService userRoleService;

    @Override
    public PageUtils<SysUserVO> list(SysUserDTO dto) {
        // 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dto.getUsername()), SysUser::getUsername, dto.getUsername())
                .like(StringUtils.isNotBlank(dto.getNickname()), SysUser::getNickname, dto.getNickname())
                .eq(dto.getStatus() != null, SysUser::getStatus, dto.getStatus())
                .like(StringUtils.isNotBlank(dto.getPhone()), SysUser::getPhone, dto.getPhone())
                .like(StringUtils.isNotBlank(dto.getEmail()), SysUser::getEmail, dto.getEmail())
                .orderByDesc(SysUser::getCreateTime);

        // 执行分页查询
        Page<SysUser> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<SysUser> result = page(page, wrapper);

        // 转换为VO对象
        List<SysUserVO> records = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 返回分页结果
        return new PageUtils<>(records, result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    public SysUserVO getInfo(Long userId) {
        SysUser user = getById(userId);
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserDTO dto) {
        // 检查用户名唯一性
        if (!checkUserNameUnique(createUserWithUsername(dto.getUsername()))) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号唯一性
        if (StringUtils.isNotEmpty(dto.getPhone()) && !checkPhoneUnique(createUserWithPhone(dto.getPhone()))) {
            throw new RuntimeException("手机号已存在");
        }

        // 检查邮箱唯一性
        if (StringUtils.isNotEmpty(dto.getEmail()) && !checkEmailUnique(createUserWithEmail(dto.getEmail()))) {
            throw new RuntimeException("邮箱已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);

        // 加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // 保存用户
        save(user);

        // 保存用户角色关联
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            insertUserRole(user.getUserId(), dto.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysUserDTO dto) {
        // 检查用户名唯一性
        if (!checkUserNameUnique(createUserWithIdAndUsername(dto.getUserId(), dto.getUsername()))) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号唯一性
        if (StringUtils.isNotEmpty(dto.getPhone()) && !checkPhoneUnique(createUserWithIdAndPhone(dto.getUserId(), dto.getPhone()))) {
            throw new RuntimeException("手机号已存在");
        }

        // 检查邮箱唯一性
        if (StringUtils.isNotEmpty(dto.getEmail()) && !checkEmailUnique(createUserWithIdAndEmail(dto.getUserId(), dto.getEmail()))) {
            throw new RuntimeException("邮箱已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);

        // 更新用户
        updateById(user);

        // 更新用户角色关联
        if (dto.getRoleIds() != null) {
            // 删除原有关联
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, user.getUserId()));

            // 添加新的关联
            if (!dto.getRoleIds().isEmpty()) {
                insertUserRole(user.getUserId(), dto.getRoleIds());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long userId) {
        // 删除用户
        removeById(userId);

        // 删除用户角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));
    }

    private SysUserVO convertToVO(SysUser user) {
        if (user == null) {
            return null;
        }
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public List<SysUser> selectUserList(SysUser user) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(user.getUsername()), SysUser::getUsername, user.getUsername())
                .eq(user.getStatus() != null, SysUser::getStatus, user.getStatus())
                .like(StringUtils.isNotBlank(user.getPhone()), SysUser::getPhone, user.getPhone())
                .like(StringUtils.isNotBlank(user.getEmail()), SysUser::getEmail, user.getEmail());
        return list(wrapper);
    }

    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return sysUserMapper.selectAllocatedList(user);
    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return sysUserMapper.selectUnallocatedList(user);
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, userName);
        return getOne(wrapper);
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return getById(userId);
    }

    @Override
    public boolean checkUserNameUnique(SysUser user) {
        Long userId = user.getUserId() == null ? -1L : user.getUserId();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, user.getUsername())
                .ne(userId > 0, SysUser::getUserId, userId);
        return count(wrapper) == 0;
    }

    @Override
    public boolean checkPhoneUnique(SysUser user) {
        Long userId = user.getUserId() == null ? -1L : user.getUserId();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, user.getPhone())
                .ne(userId > 0, SysUser::getUserId, userId);
        return count(wrapper) == 0;
    }

    @Override
    public boolean checkEmailUnique(SysUser user) {
        Long userId = user.getUserId() == null ? -1L : user.getUserId();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getEmail, user.getEmail())
                .ne(userId > 0, SysUser::getUserId, userId);
        return count(wrapper) == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        return updateById(user);
    }

    @Override
    public boolean updateUserStatus(SysUser user) {
        return updateById(user);
    }

    @Override
    public boolean updateUserProfile(SysUser user) {
        return updateById(user);
    }

    @Override
    public boolean resetPwd(SysUser user) {
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(SysUserDTO userDTO) {
        SysUser user = new SysUser();
        user.setUserId(userDTO.getUserId());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleService.deleteUserRoleByUserId(userId);
        return removeById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserByIds(Long[] userIds) {
        // 删除用户与角色关联
        userRoleService.deleteUserRoleByUserIds(userIds);
        return removeByIds(Arrays.asList(userIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(SysUserDTO userDTO) {
        SysUser user = new SysUser();
        user.setUserId(userDTO.getUserId());
        user.setStatus(userDTO.getStatus());
        updateById(user);
    }

    @Override
    public List<Long> getRoleIds(Long userId) {
        return userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(SysUserDTO userDTO) {
        // 删除原有关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userDTO.getUserId()));

        // 添加新的关联
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            insertUserRole(userDTO.getUserId(), userDTO.getRoleIds());
        }
    }

    /**
     * 创建只包含用户名的用户对象
     */
    private SysUser createUserWithUsername(String username) {
        SysUser user = new SysUser();
        user.setUsername(username);
        return user;
    }

    /**
     * 创建只包含手机号的用户对象
     */
    private SysUser createUserWithPhone(String phone) {
        SysUser user = new SysUser();
        user.setPhone(phone);
        return user;
    }

    /**
     * 创建只包含邮箱的用户对象
     */
    private SysUser createUserWithEmail(String email) {
        SysUser user = new SysUser();
        user.setEmail(email);
        return user;
    }

    /**
     * 创建包含用户ID和用户名的用户对象
     */
    private SysUser createUserWithIdAndUsername(Long userId, String username) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setUsername(username);
        return user;
    }

    /**
     * 创建包含用户ID和手机号的用户对象
     */
    private SysUser createUserWithIdAndPhone(Long userId, String phone) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setPhone(phone);
        return user;
    }

    /**
     * 创建包含用户ID和邮箱的用户对象
     */
    private SysUser createUserWithIdAndEmail(Long userId, String email) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setEmail(email);
        return user;
    }

    /**
     * 新增用户角色关联
     */
    private void insertUserRole(Long userId, List<Long> roleIds) {
        if (CollectionUtil.isNotEmpty(roleIds)) {
            userRoleMapper.batchInsert(userId, roleIds);
        }
    }

    @Override
    public LoginUser getUserInfo(String username) {
        // 查询用户信息
        SysUser user = selectUserByUserName(username);
        if (user == null) {
            return null;
        }

        // 获取用户权限信息
        Set<String> permissions = sysUserMapper.selectPermsByUserId(user.getUserId());
        // 查询角色信息
        Set<String> roles = sysUserMapper.selectRolesByUserId(user.getUserId());

        // 构建LoginUser对象
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getUserId());
        loginUser.setUsername(user.getUsername());
        loginUser.setPassword(user.getPassword());
        loginUser.setStatus(String.valueOf(user.getStatus()));
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        return loginUser;
    }
} 