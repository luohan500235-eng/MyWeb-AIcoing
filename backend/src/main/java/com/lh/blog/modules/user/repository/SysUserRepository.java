package com.lh.blog.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lh.blog.modules.user.entity.SysUser;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByUsernameAndStatus(String username, Integer status);

    boolean existsByUsername(String username);
}