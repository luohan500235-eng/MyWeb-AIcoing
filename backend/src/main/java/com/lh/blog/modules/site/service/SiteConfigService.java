package com.lh.blog.modules.site.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lh.blog.modules.site.dto.SiteConfigUpdateRequest;
import com.lh.blog.modules.site.entity.BlogSiteConfig;
import com.lh.blog.modules.site.repository.BlogSiteConfigRepository;

@Service
public class SiteConfigService {

    private final BlogSiteConfigRepository siteConfigRepository;

    public SiteConfigService(BlogSiteConfigRepository siteConfigRepository) {
        this.siteConfigRepository = siteConfigRepository;
    }

    public BlogSiteConfig getConfig() {
        return siteConfigRepository.findAll().stream().findFirst().orElseGet(() -> {
            BlogSiteConfig config = new BlogSiteConfig();
            config.setSiteName("我的技术博客");
            config.setSiteSubtitle("记录开发、阅读与生活");
            config.setSiteDescription("一个使用 React + Spring Boot 构建的个人博客");
            config.setSiteKeywords("个人博客,React,Spring Boot,MySQL");
            config.setAuthorName("lh");
            config.setAuthorIntro("一名前端和后端都想认真做好的开发者。");
            config.setAboutMd("# 关于我\n\n这里是关于页内容，你可以在后台随时编辑它。");
            return siteConfigRepository.save(config);
        });
    }

    @Transactional
    public BlogSiteConfig update(SiteConfigUpdateRequest request) {
        BlogSiteConfig entity = getConfig();
        entity.setSiteName(request.getSiteName());
        entity.setSiteSubtitle(request.getSiteSubtitle());
        entity.setSiteDescription(request.getSiteDescription());
        entity.setSiteKeywords(request.getSiteKeywords());
        entity.setLogo(request.getLogo());
        entity.setFavicon(request.getFavicon());
        entity.setAuthorName(request.getAuthorName());
        entity.setAuthorIntro(request.getAuthorIntro());
        entity.setGithubUrl(request.getGithubUrl());
        entity.setEmail(request.getEmail());
        entity.setAboutMd(request.getAboutMd());
        entity.setIcp(request.getIcp());
        return siteConfigRepository.save(entity);
    }
}