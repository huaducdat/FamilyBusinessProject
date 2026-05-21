package com.huaducdat.storemanager.payroll.controller;

import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.payroll.dto.request.SetSalaryConfigRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.payroll.service.PayrollService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
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
            HttpServletRequest request,
            @RequestParam Long userId,
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Payroll calculated")
                .data(
                        payrollService.calculate(
                                currentUser,
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
            HttpServletRequest request,
            @RequestParam Integer month,
            @RequestParam Integer year
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Monthly payroll")
                .data(
                        payrollService.monthlyPayroll(
                                currentUser,
                                month,
                                year
                        )
                )
                .build();
    }
}
