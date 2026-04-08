package com.lh.blog.modules.post.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSummaryVO {

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String coverImage;
    private Integer isTop;
    private Integer viewCount;
    private LocalDateTime publishedAt;
    private String categoryName;
    private Long categoryId;
    private List<String> tags;
}