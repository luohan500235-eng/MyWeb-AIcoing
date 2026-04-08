package com.lh.blog.modules.tag.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.tag.dto.TagSaveRequest;
import com.lh.blog.modules.tag.service.TagService;

@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/api/public/tags")
    public ApiResponse<Object> publicList() {
        return ApiResponse.success(tagService.list());
    }

    @GetMapping("/api/admin/tags")
    public ApiResponse<Object> adminList() {
        return ApiResponse.success(tagService.list());
    }

    @PostMapping("/api/admin/tags")
    public ApiResponse<Object> create(@Valid @RequestBody TagSaveRequest request) {
        request.setId(null);
        return ApiResponse.success(tagService.save(request));
    }

    @PutMapping("/api/admin/tags/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @Valid @RequestBody TagSaveRequest request) {
        request.setId(id);
        return ApiResponse.success(tagService.save(request));
    }

    @DeleteMapping("/api/admin/tags/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}