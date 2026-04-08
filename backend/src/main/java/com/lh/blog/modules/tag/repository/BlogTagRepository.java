package com.lh.blog.modules.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lh.blog.modules.tag.entity.BlogTag;

public interface BlogTagRepository extends JpaRepository<BlogTag, Long> {

    List<BlogTag> findAllByOrderByIdDesc();

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}