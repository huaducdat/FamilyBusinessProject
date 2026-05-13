package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CashRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.cash.CashFlowService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cash-flow")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    public CashFlowController(
            CashFlowService cashFlowService
    ) {

        this.cashFlowService =
                cashFlowService;
    }

    // =========================
    // INCOME
    // =========================

    @PostMapping("/income")
    public BaseResponse<?> income(
            HttpServletRequest request,
            @RequestBody CashRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        cashFlowService.income(
                currentUser,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Income success")
                .data(null)
                .build();
    }

    // =========================
    // EXPENSE
    // =========================

    @PostMapping("/expense")
    public BaseResponse<?> expense(
            HttpServletRequest request,
            @RequestBody CashRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        cashFlowService.expense(
                currentUser,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Expense success")
                .data(null)
                .build();
    }

    // =========================
    // DAILY REPORT
    // =========================

    @GetMapping("/daily-report")
    public BaseResponse<?> dailyReport() {

        return BaseResponse.builder()
                .success(true)
                .message("Daily cash report")
                .data(
                        cashFlowService
                                .dailyReport()
                )
                .build();
    }
}