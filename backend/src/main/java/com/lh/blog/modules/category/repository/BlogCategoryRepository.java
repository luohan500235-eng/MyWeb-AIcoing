package com.lh.blog.modules.category.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lh.blog.modules.category.entity.BlogCategory;

public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {

    List<BlogCategory> findAllByOrderBySortAscIdDesc();

    List<BlogCategory> findByStatusOrderBySortAscIdDesc(Integer status);

    Optional<BlogCategory> findBySlug(String slug);

    boolean existsByName(String name);

    boolean existsBySlug(String slug);
}