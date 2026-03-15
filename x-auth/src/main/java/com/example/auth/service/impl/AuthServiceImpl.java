package com.example.auth.service.impl;

import com.example.auth.domain.dto.LoginDTO;
import com.example.auth.domain.vo.TokenVO;
import com.example.auth.service.AsyncLoginLogService;
import com.example.auth.service.AuthService;
import com.example.common.core.constant.Constants;
import com.example.common.core.utils.ServletUtils;
import com.example.common.security.model.UserPrincipal;
import com.example.common.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AsyncLoginLogService asyncLoginLogService;

    /**
     * 登录失败次数限制
     */
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    /**
     * 账号锁定时间（分钟）
     */
    private static final int LOCK_TIME_MINUTES = 30;

    /**
     * 登录失败记录key前缀
     */
    private static final String LOGIN_FAIL_KEY = "login:fail:";

    /**
     * 账号锁定key前缀
     */
    private static final String ACCOUNT_LOCK_KEY = "login:lock:";

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String ipAddress = ServletUtils.getClientIP();
        String userAgent = ServletUtils.getUserAgent();

        // 1. 检查账号是否被锁定
        try {
            checkAccountLocked(username);
        } catch (LockedException e) {
            // 记录锁定状态下的登录尝试
            recordLoginLog(username, "1", e.getMessage(), ipAddress, userAgent);
            throw e;
        }

        try {
            // 2. 认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. 认证成功，清除失败记录
            clearLoginFailCount(username);

            // 4. 生成token
            String token = jwtUtils.generateToken((UserPrincipal) authentication.getPrincipal());

            // 5. 记录登录成功日志
            recordLoginLog(username, "0", "登录成功", ipAddress, userAgent);

            // 6. 返回token信息
            TokenVO tokenVO = new TokenVO();
            tokenVO.setToken(token);
            tokenVO.setTokenType(Constants.TOKEN_PREFIX.trim());
            tokenVO.setExpiresIn(jwtUtils.getExpirationDateFromToken(token).getTime());

            log.info("用户 {} 登录成功，IP: {}", username, ipAddress);
            return tokenVO;

        } catch (BadCredentialsException e) {
            // 7. 认证失败，记录失败次数和日志
            String errorMsg = handleLoginFailure(username);
            recordLoginLog(username, "1", errorMsg, ipAddress, userAgent);
            throw e;
        }
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 记录登录日志
     */
    private void recordLoginLog(String username, String status, String message, String ipAddress, String userAgent) {
        asyncLoginLogService.recordLoginLogAsync(username, status, message, ipAddress, userAgent);
    }

    /**
     * 检查账号是否被锁定
     */
    private void checkAccountLocked(String username) {
        String lockKey = ACCOUNT_LOCK_KEY + username;
        Boolean isLocked = redisTemplate.hasKey(lockKey);

        if (Boolean.TRUE.equals(isLocked)) {
            Long remainingTime = redisTemplate.getExpire(lockKey, TimeUnit.MINUTES);
            String message = String.format("账号已被锁定，请 %d 分钟后再试", remainingTime != null ? remainingTime : LOCK_TIME_MINUTES);
            log.warn("用户 {} 尝试登录但账号已被锁定，剩余时间: {} 分钟", username, remainingTime);
            throw new LockedException(message);
        }
    }

    /**
     * 处理登录失败，返回错误消息
     */
    private String handleLoginFailure(String username) {
        String failKey = LOGIN_FAIL_KEY + username;

        // 获取当前失败次数
        Integer failCount = (Integer) redisTemplate.opsForValue().get(failKey);
        failCount = failCount == null ? 0 : failCount;
        failCount++;

        // 更新失败次数（30分钟过期）
        redisTemplate.opsForValue().set(failKey, failCount, LOCK_TIME_MINUTES, TimeUnit.MINUTES);

        int remainingAttempts = MAX_LOGIN_ATTEMPTS - failCount;

        if (failCount >= MAX_LOGIN_ATTEMPTS) {
            // 锁定账号
            String lockKey = ACCOUNT_LOCK_KEY + username;
            redisTemplate.opsForValue().set(lockKey, true, LOCK_TIME_MINUTES, TimeUnit.MINUTES);

            String message = String.format("登录失败次数过多，账号已被锁定 %d 分钟", LOCK_TIME_MINUTES);
            log.warn("用户 {} 登录失败次数达到 {} 次，账号已被锁定 {} 分钟", username, failCount, LOCK_TIME_MINUTES);
            throw new LockedException(message);
        } else {
            String message = String.format("用户名或密码错误，剩余尝试次数: %d", remainingAttempts);
            log.warn("用户 {} 登录失败，当前失败次数: {}，剩余尝试次数: {}", username, failCount, remainingAttempts);
            throw new BadCredentialsException(message);
        }
    }

    /**
     * 清除登录失败记录
     */
    private void clearLoginFailCount(String username) {
        String failKey = LOGIN_FAIL_KEY + username;
        String lockKey = ACCOUNT_LOCK_KEY + username;

        redisTemplate.delete(failKey);
        redisTemplate.delete(lockKey);

        log.debug("清除用户 {} 的登录失败记录", username);
    }
} 
