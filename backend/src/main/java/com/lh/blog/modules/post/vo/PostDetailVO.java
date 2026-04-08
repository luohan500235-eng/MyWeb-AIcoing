package com.lh.blog.modules.post.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDetailVO {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String coverImage;
    private String contentMd;
    private Integer allowComment;
    private Integer viewCount;
    private String seoTitle;
    private String seoDescription;
    private LocalDateTime publishedAt;
    private String categoryName;
    private Long categoryId;
    private List<String> tags;
}