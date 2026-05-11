package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.dashboard.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {

        this.dashboardService =
                dashboardService;
    }

    @GetMapping
    public BaseResponse<?> dashboard() {

        return BaseResponse.builder()
                .success(true)
                .message("Dashboard")
                .data(
                        dashboardService.dashboard()
                )
                .build();
    }
}