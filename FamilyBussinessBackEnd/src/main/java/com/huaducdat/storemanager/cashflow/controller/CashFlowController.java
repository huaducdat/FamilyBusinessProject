package com.huaducdat.storemanager.cashflow.controller;

import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.cashflow.dto.request.CashRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.cashflow.service.CashFlowService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
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
    public BaseResponse<?> dailyReport(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Daily cash report")
                .data(
                        cashFlowService
                                .dailyReport(
                                        currentUser
                                )
                )
                .build();
    }
}
