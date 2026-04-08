package com.lh.blog.modules.auth.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.lh.blog.common.BusinessException;
import com.lh.blog.common.SecurityUtils;
import com.lh.blog.modules.auth.dto.LoginRequest;
import com.lh.blog.modules.auth.vo.LoginResponse;
import com.lh.blog.modules.user.entity.SysUser;
import com.lh.blog.modules.user.repository.SysUserRepository;
import com.lh.blog.security.JwtTokenProvider;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final SysUserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
            SysUserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        SysUser user = userRepository.findByUsernameAndStatus(userDetails.getUsername(), 1)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);
        String token = jwtTokenProvider.generateToken(userDetails);
        return new LoginResponse(token, user.getUsername(), user.getNickname());
    }

    public Map<String, Object> currentUser() {
        String username = SecurityUtils.currentUsername();
        SysUser user = userRepository.findByUsernameAndStatus(username, 1)
                .orElseThrow(() -> new BusinessException("登录已失效"));
        Map<String, Object> profile = new LinkedHashMap<String, Object>();
        profile.put("id", user.getId());
        profile.put("username", user.getUsername());
        profile.put("nickname", user.getNickname());
        profile.put("avatar", user.getAvatar());
        profile.put("email", user.getEmail());
        return profile;
    }
}