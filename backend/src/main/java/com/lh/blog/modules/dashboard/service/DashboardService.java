package com.lh.blog.modules.dashboard.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lh.blog.modules.category.repository.BlogCategoryRepository;
import com.lh.blog.modules.comment.repository.BlogCommentRepository;
import com.lh.blog.modules.post.repository.BlogPostRepository;
import com.lh.blog.modules.tag.repository.BlogTagRepository;

@Service
public class DashboardService {

    private final BlogPostRepository postRepository;
    private final BlogCategoryRepository categoryRepository;
    private final BlogTagRepository tagRepository;
    private final BlogCommentRepository commentRepository;

    public DashboardService(BlogPostRepository postRepository, BlogCategoryRepository categoryRepository,
            BlogTagRepository tagRepository, BlogCommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.commentRepository = commentRepository;
    }

    public Map<String, Object> stats() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("postCount", postRepository.count());
        data.put("categoryCount", categoryRepository.count());
        data.put("tagCount", tagRepository.count());
        data.put("pendingCommentCount", commentRepository.countByStatus(0));
        return data;
    }
}