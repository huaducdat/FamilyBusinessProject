package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.report.ReportService;
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
    public BaseResponse<?> monthlyRevenue() {

        return BaseResponse.builder()
                .success(true)
                .message("Monthly revenue")
                .data(
                        reportService
                                .monthlyRevenue()
                )
                .build();
    }

    @GetMapping("/best-seller")
    public BaseResponse<?> bestSeller() {

        return BaseResponse.builder()
                .success(true)
                .message("Best seller")
                .data(
                        reportService.bestSeller()
                )
                .build();
    }

    @GetMapping("/profit")
    public BaseResponse<?> profit() {

        return BaseResponse.builder()
                .success(true)
                .message("Profit report")
                .data(
                        reportService
                                .profitReport()
                )
                .build();
    }
}