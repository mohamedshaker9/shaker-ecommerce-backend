package com.shaker.shakerecommerce.service;

import com.shaker.shakerecommerce.dto.AdminDashboardStatsResponse;
import com.shaker.shakerecommerce.repo.CategoryRepo;
import com.shaker.shakerecommerce.repo.IOrderRepo;
import com.shaker.shakerecommerce.repo.IProductRepo;
import com.shaker.shakerecommerce.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final IOrderRepo orderRepo;
    private final UserRepo userRepo;
    private final IProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Transactional(readOnly = true)
    public AdminDashboardStatsResponse getDashboardStats() {
        return new AdminDashboardStatsResponse(
                orderRepo.count(),
                orderRepo.sumTotalRevenue(),
                userRepo.count(),
                productRepo.count(),
                categoryRepo.count()
        );
    }
}