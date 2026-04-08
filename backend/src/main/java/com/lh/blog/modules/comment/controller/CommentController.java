package com.lh.blog.modules.comment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.comment.dto.CommentSubmitRequest;
import com.lh.blog.modules.comment.service.CommentService;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/public/comments")
    public ApiResponse<Object> publicList(@RequestParam Long postId) {
        return ApiResponse.success(commentService.publicList(postId));
    }

    @PostMapping("/api/public/comments")
    public ApiResponse<Void> submit(@Valid @RequestBody CommentSubmitRequest request, HttpServletRequest httpServletRequest) {
        commentService.submit(request, httpServletRequest);
        return ApiResponse.success("评论已提交，等待审核", null);
    }

    @GetMapping("/api/admin/comments")
    public ApiResponse<Object> adminPage(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer status) {
        return ApiResponse.success(commentService.adminPage(page, size, status));
    }

    @PutMapping("/api/admin/comments/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        commentService.updateStatus(id, status);
        return ApiResponse.success("更新成功", null);
    }
}