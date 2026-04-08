package com.lh.blog.modules.post.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.lh.blog.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "blog_post")
public class BlogPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(length = 500)
    private String summary;

    @Column(name = "cover_image", length = 255)
    private String coverImage;

    @Lob
    @Column(name = "content_md", nullable = false, columnDefinition = "LONGTEXT")
    private String contentMd;

    @Lob
    @Column(name = "content_html", columnDefinition = "LONGTEXT")
    private String contentHtml;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer status = 0;

    @Column(name = "is_top", nullable = false, columnDefinition = "TINYINT")
    private Integer isTop = 0;

    @Column(name = "allow_comment", nullable = false, columnDefinition = "TINYINT")
    private Integer allowComment = 1;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "seo_title", length = 255)
    private String seoTitle;

    @Column(name = "seo_description", length = 500)
    private String seoDescription;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer deleted = 0;
}
