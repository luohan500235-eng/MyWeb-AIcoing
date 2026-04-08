package com.lh.blog.modules.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.lh.blog.modules.comment.entity.BlogComment;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long>, JpaSpecificationExecutor<BlogComment> {

    List<BlogComment> findByPostIdAndStatusOrderByCreatedAtAsc(Long postId, Integer status);

    long countByStatus(Integer status);
}