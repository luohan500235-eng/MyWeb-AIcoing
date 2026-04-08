package com.lh.blog.modules.category.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.category.dto.CategorySaveRequest;
import com.lh.blog.modules.category.service.CategoryService;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/public/categories")
    public ApiResponse<Object> publicList() {
        return ApiResponse.success(categoryService.publicList());
    }

    @GetMapping("/api/admin/categories")
    public ApiResponse<Object> adminList() {
        return ApiResponse.success(categoryService.adminList());
    }

    @PostMapping("/api/admin/categories")
    public ApiResponse<Object> create(@Valid @RequestBody CategorySaveRequest request) {
        request.setId(null);
        return ApiResponse.success(categoryService.save(request));
    }

    @PutMapping("/api/admin/categories/{id}")
    public ApiResponse<Object> update(@PathVariable Long id, @Valid @RequestBody CategorySaveRequest request) {
        request.setId(id);
        return ApiResponse.success(categoryService.save(request));
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success("删除成功", null);
    }
}