package com.lh.blog.modules.site.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.site.dto.SiteConfigUpdateRequest;
import com.lh.blog.modules.site.service.SiteConfigService;

@RestController
public class SiteConfigController {

    private final SiteConfigService siteConfigService;

    public SiteConfigController(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    @GetMapping("/api/public/site-config")
    public ApiResponse<Object> publicConfig() {
        return ApiResponse.success(siteConfigService.getConfig());
    }

    @GetMapping("/api/admin/site-config")
    public ApiResponse<Object> adminConfig() {
        return ApiResponse.success(siteConfigService.getConfig());
    }

    @PutMapping("/api/admin/site-config")
    public ApiResponse<Object> update(@RequestBody SiteConfigUpdateRequest request) {
        return ApiResponse.success(siteConfigService.update(request));
    }
}