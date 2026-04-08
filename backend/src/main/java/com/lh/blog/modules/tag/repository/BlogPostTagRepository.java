package com.lh.blog.modules.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lh.blog.modules.tag.entity.BlogPostTag;

public interface BlogPostTagRepository extends JpaRepository<BlogPostTag, Long> {

    List<BlogPostTag> findByPostId(Long postId);

    List<BlogPostTag> findByPostIdIn(List<Long> postIds);

    List<BlogPostTag> findByTagId(Long tagId);

    void deleteByPostId(Long postId);
}