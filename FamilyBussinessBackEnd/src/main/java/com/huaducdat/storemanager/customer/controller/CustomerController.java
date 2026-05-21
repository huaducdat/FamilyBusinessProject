package com.huaducdat.storemanager.customer.controller;

import com.huaducdat.storemanager.customer.model.entity.Customer;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.customer.dto.request.CreateCustomerRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.customer.service.CustomerService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.huaducdat.storemanager.customer.dto.request.DebtPaymentRequest;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService
    ) {

        this.customerService =
                customerService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public BaseResponse<?> create(
            HttpServletRequest request,
            @RequestBody CreateCustomerRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Customer customer =
                customerService.create(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Customer created")
                .data(
                        customer.getFullName()
                )
                .build();
    }

    // =========================
    // LIST
    // =========================

    @GetMapping
    public BaseResponse<?> list(
            HttpServletRequest request
    ) {
        User currentUser =
                CurrentUserUtil.get(
                        request
                );
        return BaseResponse.builder()
                .success(true)
                .message("Customer list")
                .data(
                        customerService.list(
                                currentUser
                        )
                )
                .build();
    }

    @PostMapping("/pay-debt")
    public BaseResponse<?> payDebt(
            HttpServletRequest request,
            @RequestBody DebtPaymentRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Debt payment success")
                .data(
                        customerService.payDebt(
                                currentUser,
                                body
                        )
                )
                .build();
    }

    @GetMapping("/{id}/history")
    public BaseResponse<?> history(
            HttpServletRequest request,
            @PathVariable Long id
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Customer history")
                .data(
                        customerService.history(
                                currentUser,
                                id
                        )
                )
                .build();
    }
}
