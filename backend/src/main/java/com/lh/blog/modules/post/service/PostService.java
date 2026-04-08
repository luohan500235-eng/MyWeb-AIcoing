package com.lh.blog.modules.post.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.lh.blog.common.BusinessException;
import com.lh.blog.common.PageResponse;
import com.lh.blog.modules.category.entity.BlogCategory;
import com.lh.blog.modules.category.repository.BlogCategoryRepository;
import com.lh.blog.modules.post.dto.PostSaveRequest;
import com.lh.blog.modules.post.entity.BlogPost;
import com.lh.blog.modules.post.repository.BlogPostRepository;
import com.lh.blog.modules.post.vo.AdminPostVO;
import com.lh.blog.modules.post.vo.PostDetailVO;
import com.lh.blog.modules.post.vo.PostSummaryVO;
import com.lh.blog.modules.tag.entity.BlogPostTag;
import com.lh.blog.modules.tag.entity.BlogTag;
import com.lh.blog.modules.tag.repository.BlogPostTagRepository;
import com.lh.blog.modules.tag.repository.BlogTagRepository;

@Service
public class PostService {

    private final BlogPostRepository postRepository;
    private final BlogCategoryRepository categoryRepository;
    private final BlogTagRepository tagRepository;
    private final BlogPostTagRepository postTagRepository;

    public PostService(BlogPostRepository postRepository, BlogCategoryRepository categoryRepository,
            BlogTagRepository tagRepository, BlogPostTagRepository postTagRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
    }

    public PageResponse<AdminPostVO> adminPage(int page, int size, String keyword, Integer status) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size,
                Sort.by(Sort.Order.desc("isTop"), Sort.Order.desc("publishedAt"), Sort.Order.desc("id")));
        Specification<BlogPost> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.or(cb.like(root.get("title"), "%" + keyword.trim() + "%"),
                        cb.like(root.get("summary"), "%" + keyword.trim() + "%")));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<BlogPost> result = postRepository.findAll(specification, pageable);
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(result.getContent());
        Map<Long, List<BlogTag>> tagMap = loadTagMap(result.getContent().stream().map(BlogPost::getId).collect(Collectors.toList()));
        return PageResponse.from(result.map(post -> toAdminVO(post, categoryMap, tagMap)));
    }

    public PageResponse<PostSummaryVO> publicPage(int page, int size, String keyword, Long categoryId, Long tagId) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size,
                Sort.by(Sort.Order.desc("isTop"), Sort.Order.desc("publishedAt"), Sort.Order.desc("id")));
        Set<Long> matchedPostIds = null;
        if (tagId != null) {
            matchedPostIds = postTagRepository.findByTagId(tagId).stream().map(BlogPostTag::getPostId).collect(Collectors.toSet());
            if (matchedPostIds.isEmpty()) {
                return new PageResponse<PostSummaryVO>(Collections.<PostSummaryVO>emptyList(), 0, page, size, 0);
            }
        }
        final Set<Long> finalMatchedPostIds = matchedPostIds;
        Specification<BlogPost> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(cb.equal(root.get("deleted"), 0));
            predicates.add(cb.equal(root.get("status"), 1));
            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.or(cb.like(root.get("title"), "%" + keyword.trim() + "%"),
                        cb.like(root.get("summary"), "%" + keyword.trim() + "%")));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId));
            }
            if (finalMatchedPostIds != null) {
                predicates.add(root.get("id").in(finalMatchedPostIds));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<BlogPost> result = postRepository.findAll(specification, pageable);
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(result.getContent());
        Map<Long, List<BlogTag>> tagMap = loadTagMap(result.getContent().stream().map(BlogPost::getId).collect(Collectors.toList()));
        return PageResponse.from(result.map(post -> toSummaryVO(post, categoryMap, tagMap)));
    }

    @Transactional
    public AdminPostVO save(PostSaveRequest request) {
        postRepository.findAll().stream()
                .filter(post -> post.getDeleted() == 0)
                .filter(post -> request.getId() == null || !post.getId().equals(request.getId()))
                .filter(post -> post.getSlug().equals(request.getSlug()))
                .findFirst()
                .ifPresent(post -> {
                    throw new BusinessException("slug 已存在，请更换");
                });

        if (request.getCategoryId() != null && !categoryRepository.existsById(request.getCategoryId())) {
            throw new BusinessException("分类不存在");
        }

        BlogPost entity = request.getId() == null ? new BlogPost()
                : postRepository.findByIdAndDeleted(request.getId(), 0)
                        .orElseThrow(() -> new BusinessException("文章不存在"));

        entity.setTitle(request.getTitle());
        entity.setSlug(request.getSlug());
        entity.setSummary(request.getSummary());
        entity.setCoverImage(request.getCoverImage());
        entity.setContentMd(request.getContentMd());
        entity.setCategoryId(request.getCategoryId());
        entity.setStatus(request.getStatus());
        entity.setIsTop(request.getIsTop() == null ? 0 : request.getIsTop());
        entity.setAllowComment(request.getAllowComment() == null ? 1 : request.getAllowComment());
        entity.setSeoTitle(request.getSeoTitle());
        entity.setSeoDescription(request.getSeoDescription());
        if (entity.getStatus() == 1 && entity.getPublishedAt() == null) {
            entity.setPublishedAt(LocalDateTime.now());
        }
        BlogPost saved = postRepository.save(entity);
        postTagRepository.deleteByPostId(saved.getId());
        if (request.getTagIds() != null) {
            for (Long tagId : request.getTagIds()) {
                if (tagRepository.existsById(tagId)) {
                    BlogPostTag relation = new BlogPostTag();
                    relation.setPostId(saved.getId());
                    relation.setTagId(tagId);
                    postTagRepository.save(relation);
                }
            }
        }
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(Collections.singletonList(saved));
        Map<Long, List<BlogTag>> tagMap = loadTagMap(Collections.singletonList(saved.getId()));
        return toAdminVO(saved, categoryMap, tagMap);
    }

    @Transactional
    public void delete(Long id) {
        BlogPost post = postRepository.findByIdAndDeleted(id, 0).orElseThrow(() -> new BusinessException("文章不存在"));
        post.setDeleted(1);
        postRepository.save(post);
        postTagRepository.deleteByPostId(id);
    }

    public AdminPostVO adminDetail(Long id) {
        BlogPost post = postRepository.findByIdAndDeleted(id, 0).orElseThrow(() -> new BusinessException("文章不存在"));
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(Collections.singletonList(post));
        Map<Long, List<BlogTag>> tagMap = loadTagMap(Collections.singletonList(post.getId()));
        return toAdminVO(post, categoryMap, tagMap);
    }

    @Transactional
    public PostDetailVO publicDetail(String slug) {
        BlogPost post = postRepository.findBySlugAndStatusAndDeleted(slug, 1, 0)
                .orElseThrow(() -> new BusinessException("文章不存在或尚未发布"));
        post.setViewCount(post.getViewCount() + 1);
        BlogPost updated = postRepository.save(post);
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(Collections.singletonList(updated));
        Map<Long, List<BlogTag>> tagMap = loadTagMap(Collections.singletonList(updated.getId()));
        return toDetailVO(updated, categoryMap, tagMap);
    }

    public List<Map<String, Object>> archives() {
        Specification<BlogPost> specification = (root, query, cb) -> cb.and(cb.equal(root.get("deleted"), 0),
                cb.equal(root.get("status"), 1));
        List<BlogPost> posts = postRepository.findAll(specification,
                Sort.by(Sort.Order.desc("publishedAt"), Sort.Order.desc("id")));
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(posts);
        Map<Long, List<BlogTag>> tagMap = loadTagMap(posts.stream().map(BlogPost::getId).collect(Collectors.toList()));
        return posts.stream().map(post -> {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("yearMonth",
                    post.getPublishedAt() == null ? null : post.getPublishedAt().toString().substring(0, 7));
            item.put("post", toSummaryVO(post, categoryMap, tagMap));
            return item;
        }).collect(Collectors.toList());
    }

    public List<PostSummaryVO> latest(int limit) {
        Pageable pageable = PageRequest.of(0, limit,
                Sort.by(Sort.Order.desc("isTop"), Sort.Order.desc("publishedAt"), Sort.Order.desc("id")));
        Specification<BlogPost> specification = (root, query, cb) -> cb.and(cb.equal(root.get("deleted"), 0),
                cb.equal(root.get("status"), 1));
        Page<BlogPost> page = postRepository.findAll(specification, pageable);
        Map<Long, BlogCategory> categoryMap = loadCategoryMap(page.getContent());
        Map<Long, List<BlogTag>> tagMap = loadTagMap(page.getContent().stream().map(BlogPost::getId).collect(Collectors.toList()));
        return page.getContent().stream().map(post -> toSummaryVO(post, categoryMap, tagMap)).collect(Collectors.toList());
    }

    private Map<Long, BlogCategory> loadCategoryMap(List<BlogPost> posts) {
        List<Long> categoryIds = posts.stream().map(BlogPost::getCategoryId).filter(id -> id != null).distinct().collect(Collectors.toList());
        if (categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return categoryRepository.findAllById(categoryIds).stream().collect(Collectors.toMap(BlogCategory::getId, category -> category));
    }

    private Map<Long, List<BlogTag>> loadTagMap(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<BlogPostTag> relations = postTagRepository.findByPostIdIn(postIds);
        if (relations.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Long> tagIds = relations.stream().map(BlogPostTag::getTagId).distinct().collect(Collectors.toList());
        Map<Long, BlogTag> tags = tagRepository.findAllById(tagIds).stream().collect(Collectors.toMap(BlogTag::getId, tag -> tag));
        Map<Long, List<BlogTag>> tagMap = new HashMap<Long, List<BlogTag>>();
        for (BlogPostTag relation : relations) {
            BlogTag tag = tags.get(relation.getTagId());
            if (tag == null) {
                continue;
            }
            tagMap.computeIfAbsent(relation.getPostId(), key -> new ArrayList<BlogTag>()).add(tag);
        }
        return tagMap;
    }

    private AdminPostVO toAdminVO(BlogPost post, Map<Long, BlogCategory> categoryMap, Map<Long, List<BlogTag>> tagMap) {
        BlogCategory category = post.getCategoryId() == null ? null : categoryMap.get(post.getCategoryId());
        List<BlogTag> tags = tagMap.getOrDefault(post.getId(), Collections.<BlogTag>emptyList());
        return AdminPostVO.builder().id(post.getId()).title(post.getTitle()).slug(post.getSlug()).summary(post.getSummary())
                .coverImage(post.getCoverImage()).contentMd(post.getContentMd()).categoryId(post.getCategoryId())
                .categoryName(category == null ? null : category.getName()).status(post.getStatus()).isTop(post.getIsTop())
                .allowComment(post.getAllowComment()).viewCount(post.getViewCount()).seoTitle(post.getSeoTitle())
                .seoDescription(post.getSeoDescription()).publishedAt(post.getPublishedAt()).createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt()).tagIds(tags.stream().map(BlogTag::getId).collect(Collectors.toList()))
                .tags(tags.stream().map(BlogTag::getName).collect(Collectors.toList())).build();
    }

    private PostSummaryVO toSummaryVO(BlogPost post, Map<Long, BlogCategory> categoryMap, Map<Long, List<BlogTag>> tagMap) {
        BlogCategory category = post.getCategoryId() == null ? null : categoryMap.get(post.getCategoryId());
        List<BlogTag> tags = tagMap.getOrDefault(post.getId(), Collections.<BlogTag>emptyList());
        return PostSummaryVO.builder().id(post.getId()).title(post.getTitle()).slug(post.getSlug()).summary(post.getSummary())
                .coverImage(post.getCoverImage()).isTop(post.getIsTop()).viewCount(post.getViewCount())
                .publishedAt(post.getPublishedAt()).categoryName(category == null ? null : category.getName())
                .categoryId(post.getCategoryId()).tags(tags.stream().map(BlogTag::getName).collect(Collectors.toList())).build();
    }

    private PostDetailVO toDetailVO(BlogPost post, Map<Long, BlogCategory> categoryMap, Map<Long, List<BlogTag>> tagMap) {
        BlogCategory category = post.getCategoryId() == null ? null : categoryMap.get(post.getCategoryId());
        List<BlogTag> tags = tagMap.getOrDefault(post.getId(), Collections.<BlogTag>emptyList());
        return PostDetailVO.builder().id(post.getId()).title(post.getTitle()).slug(post.getSlug()).summary(post.getSummary())
                .coverImage(post.getCoverImage()).contentMd(post.getContentMd()).allowComment(post.getAllowComment())
                .viewCount(post.getViewCount()).seoTitle(post.getSeoTitle()).seoDescription(post.getSeoDescription())
                .publishedAt(post.getPublishedAt()).categoryName(category == null ? null : category.getName())
                .categoryId(post.getCategoryId()).tags(tags.stream().map(BlogTag::getName).collect(Collectors.toList())).build();
    }
}