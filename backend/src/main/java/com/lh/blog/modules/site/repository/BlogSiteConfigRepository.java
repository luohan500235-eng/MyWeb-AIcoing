package com.lh.blog.modules.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lh.blog.modules.site.entity.BlogSiteConfig;

public interface BlogSiteConfigRepository extends JpaRepository<BlogSiteConfig, Long> {
}