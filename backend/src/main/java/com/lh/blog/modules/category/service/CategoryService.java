package com.lh.blog.modules.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lh.blog.common.BusinessException;
import com.lh.blog.modules.category.dto.CategorySaveRequest;
import com.lh.blog.modules.category.entity.BlogCategory;
import com.lh.blog.modules.category.repository.BlogCategoryRepository;

@Service
public class CategoryService {

    private final BlogCategoryRepository categoryRepository;

    public CategoryService(BlogCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<BlogCategory> publicList() {
        return categoryRepository.findByStatusOrderBySortAscIdDesc(1);
    }

    public List<BlogCategory> adminList() {
        return categoryRepository.findAllByOrderBySortAscIdDesc();
    }

    @Transactional
    public BlogCategory save(CategorySaveRequest request) {
        BlogCategory entity = request.getId() == null ? new BlogCategory()
                : categoryRepository.findById(request.getId()).orElseThrow(() -> new BusinessException("分类不存在"));

        categoryRepository.findAll().stream()
                .filter(category -> entity.getId() == null || !category.getId().equals(entity.getId()))
                .filter(category -> category.getName().equals(request.getName()) || category.getSlug().equals(request.getSlug()))
                .findFirst()
                .ifPresent(category -> {
                    throw new BusinessException("分类名称或 slug 已存在");
                });

        entity.setName(request.getName());
        entity.setSlug(request.getSlug());
        entity.setDescription(request.getDescription());
        entity.setSort(request.getSort());
        entity.setStatus(request.getStatus());
        return categoryRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new BusinessException("分类不存在");
        }
        categoryRepository.deleteById(id);
    }
}