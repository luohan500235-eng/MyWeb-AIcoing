package com.lh.blog.modules.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lh.blog.modules.post.entity.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long>, JpaSpecificationExecutor<BlogPost> {

    Optional<BlogPost> findBySlugAndStatusAndDeleted(String slug, Integer status, Integer deleted);

    Optional<BlogPost> findByIdAndDeleted(Long id, Integer deleted);

    boolean existsBySlug(String slug);
}