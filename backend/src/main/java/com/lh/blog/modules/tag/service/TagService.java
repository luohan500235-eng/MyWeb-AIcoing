package com.lh.blog.modules.tag.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lh.blog.common.BusinessException;
import com.lh.blog.modules.tag.dto.TagSaveRequest;
import com.lh.blog.modules.tag.entity.BlogTag;
import com.lh.blog.modules.tag.repository.BlogTagRepository;

@Service
public class TagService {

    private final BlogTagRepository tagRepository;

    public TagService(BlogTagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<BlogTag> list() {
        return tagRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public BlogTag save(TagSaveRequest request) {
        BlogTag entity = request.getId() == null ? new BlogTag()
                : tagRepository.findById(request.getId()).orElseThrow(() -> new BusinessException("标签不存在"));

        tagRepository.findAll().stream()
                .filter(tag -> entity.getId() == null || !tag.getId().equals(entity.getId()))
                .filter(tag -> tag.getName().equals(request.getName()) || tag.getSlug().equals(request.getSlug()))
                .findFirst()
                .ifPresent(tag -> {
                    throw new BusinessException("标签名称或 slug 已存在");
                });

        entity.setName(request.getName());
        entity.setSlug(request.getSlug());
        entity.setColor(request.getColor());
        return tagRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new BusinessException("标签不存在");
        }
        tagRepository.deleteById(id);
    }
}