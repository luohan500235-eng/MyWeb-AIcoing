package com.lh.blog.modules.dashboard.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.dashboard.service.DashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ApiResponse<Object> stats() {
        return ApiResponse.success(dashboardService.stats());
    }
}