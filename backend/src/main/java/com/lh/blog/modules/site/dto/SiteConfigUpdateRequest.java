package com.lh.blog.modules.site.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteConfigUpdateRequest {

    private String siteName;
    private String siteSubtitle;
    private String siteDescription;
    private String siteKeywords;
    private String logo;
    private String favicon;
    private String authorName;
    private String authorIntro;
    private String githubUrl;
    private String email;
    private String aboutMd;
    private String icp;
}