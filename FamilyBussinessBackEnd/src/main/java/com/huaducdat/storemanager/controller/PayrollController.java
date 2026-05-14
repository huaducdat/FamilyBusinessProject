package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.SetSalaryConfigRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.payroll.PayrollService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(
            PayrollService payrollService
    ) {

        this.payrollService =
                payrollService;
    }

    // =========================
    // SET CONFIG
    // =========================

    @PostMapping("/config")
    public BaseResponse<?> setConfig(
            HttpServletRequest request,
            @RequestBody SetSalaryConfigRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        payrollService.setConfig(
                currentUser,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Salary config updated")
                .data(null)
                .build();
    }

    // =========================
    // CALCULATE
    // =========================

    @PostMapping("/calculate")
    public BaseResponse<?> calculate(
            @RequestParam Long userId,
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {

        return BaseResponse.builder()
                .success(true)
                .message("Payroll calculated")
                .data(
                        payrollService.calculate(
                                userId,
                                month,
                                year
                        )
                )
                .build();
    }

    // =========================
    // MONTHLY REPORT
    // =========================

    @GetMapping("/monthly")
    public BaseResponse<?> monthly(
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {

        return BaseResponse.builder()
                .success(true)
                .message("Monthly payroll")
                .data(
                        payrollService.monthlyPayroll(
                                month,
                                year
                        )
                )
                .build();
    }
}