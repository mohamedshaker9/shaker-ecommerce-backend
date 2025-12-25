package com.shaker.shakerecommerce.dto;

import java.math.BigDecimal;

public record AdminDashboardStatsResponse(
        long totalOrders,
        BigDecimal totalRevenue,
        long totalUsers,
        long totalProducts,
        long totalCategories
) {
}