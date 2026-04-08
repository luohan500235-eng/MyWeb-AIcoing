package com.lh.blog.modules.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lh.blog.modules.file.entity.SysFile;

public interface SysFileRepository extends JpaRepository<SysFile, Long> {
}