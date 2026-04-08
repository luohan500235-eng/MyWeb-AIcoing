package com.lh.blog.modules.comment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lh.blog.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "blog_comment")
public class BlogComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "parent_id", nullable = false)
    private Long parentId = 0L;

    @Column(name = "reply_to_id")
    private Long replyToId;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String website;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 64)
    private String ip;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer status = 0;
}
