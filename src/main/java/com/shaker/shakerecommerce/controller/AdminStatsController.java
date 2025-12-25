package com.shaker.shakerecommerce.controller;

import com.shaker.shakerecommerce.dto.AdminDashboardStatsResponse;
import com.shaker.shakerecommerce.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping
    public AdminDashboardStatsResponse getStats() {
        return adminStatsService.getDashboardStats();
    }
}