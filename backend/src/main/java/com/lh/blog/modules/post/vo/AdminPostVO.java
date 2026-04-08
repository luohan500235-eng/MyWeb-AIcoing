package com.lh.blog.modules.post.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminPostVO {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String coverImage;
    private String contentMd;
    private Long categoryId;
    private String categoryName;
    private Integer status;
    private Integer isTop;
    private Integer allowComment;
    private Integer viewCount;
    private String seoTitle;
    private String seoDescription;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> tagIds;
    private List<String> tags;
}