package com.lh.blog.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lh.blog.modules.user.entity.SysUser;
import com.lh.blog.modules.user.repository.SysUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserRepository sysUserRepository;

    public CustomUserDetailsService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserRepository.findByUsernameAndStatus(username, 1)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在或已禁用"));
        return new User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    }
}