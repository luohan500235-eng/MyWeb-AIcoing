package com.lh.blog.modules.post.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.post.dto.PostSaveRequest;
import com.lh.blog.modules.post.service.PostService;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/public/posts")
    public ApiResponse<Object> publicPage(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId) {
        return ApiResponse.success(postService.publicPage(page, size, keyword, categoryId, tagId));
    }

    @GetMapping("/api/public/posts/latest")
    public ApiResponse<Object> latest(@RequestParam(defaultValue = "6") int limit) {
        return ApiResponse.success(postService.latest(limit));
    }

    @GetMapping("/api/public/posts/{slug}")
    public ApiResponse<Object> detail(@PathVariable String slug) {
        return ApiResponse.success(postService.publicDetail(slug));
    }

    @GetMapping("/api/public/archives")
    public ApiResponse<Object> archives() {
        return ApiResponse.success(postService.archives());
    }

    @GetMapping("/api/admin/posts")
    public ApiResponse<Object> adminPage(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return ApiResponse.success(postService.adminPage(page, size, keyword, status));
    }

    @GetMapping("/api/admin/posts/{id}")
    public ApiResponse<Object> adminDetail(@PathVariable Long id) {
        return ApiResponse.success(postService.adminDetail(id));
    }

    @PostMapping("/api/admin/posts")
    public ApiResponse<Object> create(@Valid @RequestBody PostSaveRequest request) {
        request.setId(null);
        return ApiResponse.success(postService.save(request));
    }

    @PutMapping("/api/admin/posts/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @Valid @RequestBody PostSaveRequest request) {
        request.setId(id);
        return ApiResponse.success(postService.save(request));
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}