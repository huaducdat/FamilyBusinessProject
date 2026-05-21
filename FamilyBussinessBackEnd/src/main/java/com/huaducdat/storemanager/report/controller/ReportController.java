package com.huaducdat.storemanager.report.controller;

import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.report.service.ReportService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService
    ) {

        this.reportService =
                reportService;
    }

    // =========================
    // MONTHLY REVENUE
    // =========================

    @GetMapping("/monthly-revenue")
    public BaseResponse<?> monthlyRevenue(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Monthly revenue")
                .data(
                        reportService
                                .monthlyRevenue(
                                        currentUser
                                )
                )
                .build();
    }

    @GetMapping("/best-seller")
    public BaseResponse<?> bestSeller(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Best seller")
                .data(
                        reportService.bestSeller(
                                currentUser
                        )
                )
                .build();
    }

    @GetMapping("/profit")
    public BaseResponse<?> profit(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Profit report")
                .data(
                        reportService
                                .profitReport(
                                        currentUser
                                )
                )
                .build();
    }
}
