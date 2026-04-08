package com.lh.blog.modules.site.entity;

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
@Table(name = "blog_site_config")
public class BlogSiteConfig extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_name", length = 100)
    private String siteName;

    @Column(name = "site_subtitle", length = 255)
    private String siteSubtitle;

    @Column(name = "site_description", length = 500)
    private String siteDescription;

    @Column(name = "site_keywords", length = 255)
    private String siteKeywords;

    @Column(length = 255)
    private String logo;

    @Column(length = 255)
    private String favicon;

    @Column(name = "author_name", length = 50)
    private String authorName;

    @Column(name = "author_intro", length = 500)
    private String authorIntro;

    @Column(name = "github_url", length = 255)
    private String githubUrl;

    @Column(length = 100)
    private String email;

    @Lob
    @Column(name = "about_md", columnDefinition = "LONGTEXT")
    private String aboutMd;

    @Column(length = 100)
    private String icp;
}