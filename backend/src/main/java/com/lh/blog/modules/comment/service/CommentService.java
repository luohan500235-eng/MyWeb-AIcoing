package com.lh.blog.modules.comment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lh.blog.common.BusinessException;
import com.lh.blog.common.PageResponse;
import com.lh.blog.modules.comment.dto.CommentSubmitRequest;
import com.lh.blog.modules.comment.entity.BlogComment;
import com.lh.blog.modules.comment.repository.BlogCommentRepository;
import com.lh.blog.modules.post.entity.BlogPost;
import com.lh.blog.modules.post.repository.BlogPostRepository;

@Service
public class CommentService {

    private final BlogCommentRepository commentRepository;
    private final BlogPostRepository postRepository;

    public CommentService(BlogCommentRepository commentRepository, BlogPostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<Map<String, Object>> publicList(Long postId) {
        return commentRepository.findByPostIdAndStatusOrderByCreatedAtAsc(postId, 1).stream().map(comment -> {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", comment.getId());
            item.put("postId", comment.getPostId());
            item.put("parentId", comment.getParentId());
            item.put("replyToId", comment.getReplyToId());
            item.put("nickname", comment.getNickname());
            item.put("website", comment.getWebsite());
            item.put("content", comment.getContent());
            item.put("createdAt", comment.getCreatedAt());
            return item;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void submit(CommentSubmitRequest request, HttpServletRequest httpServletRequest) {
        BlogPost post = postRepository.findByIdAndDeleted(request.getPostId(), 0)
                .orElseThrow(() -> new BusinessException("文章不存在"));
        if (post.getStatus() != 1) {
            throw new BusinessException("文章尚未发布，不能评论");
        }
        if (post.getAllowComment() != 1) {
            throw new BusinessException("该文章已关闭评论");
        }
        BlogComment comment = new BlogComment();
        comment.setPostId(request.getPostId());
        comment.setParentId(request.getParentId() == null ? 0L : request.getParentId());
        comment.setReplyToId(request.getReplyToId());
        comment.setNickname(request.getNickname());
        comment.setEmail(request.getEmail());
        comment.setWebsite(request.getWebsite());
        comment.setContent(request.getContent());
        comment.setIp(httpServletRequest.getRemoteAddr());
        comment.setUserAgent(httpServletRequest.getHeader("User-Agent"));
        comment.setStatus(0);
        commentRepository.save(comment);
    }

    public PageResponse<Map<String, Object>> adminPage(int page, int size, Integer status) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(Sort.Order.desc("id")));
        Specification<BlogComment> specification = (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            Predicate predicate = cb.equal(root.get("status"), status);
            return cb.and(predicate);
        };
        Page<BlogComment> result = commentRepository.findAll(specification, pageable);
        Map<Long, String> postTitleMap = postRepository.findAllById(
                result.getContent().stream().map(BlogComment::getPostId).distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(BlogPost::getId, BlogPost::getTitle));
        Page<Map<String, Object>> converted = result.map(comment -> {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", comment.getId());
            item.put("postId", comment.getPostId());
            item.put("postTitle", postTitleMap.get(comment.getPostId()));
            item.put("nickname", comment.getNickname());
            item.put("email", comment.getEmail());
            item.put("website", comment.getWebsite());
            item.put("content", comment.getContent());
            item.put("status", comment.getStatus());
            item.put("createdAt", comment.getCreatedAt());
            return item;
        });
        return PageResponse.from(converted);
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        BlogComment comment = commentRepository.findById(id).orElseThrow(() -> new BusinessException("评论不存在"));
        comment.setStatus(status);
        commentRepository.save(comment);
    }
}