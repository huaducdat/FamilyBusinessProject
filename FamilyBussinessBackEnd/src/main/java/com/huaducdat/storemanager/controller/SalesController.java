package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateInvoiceRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.model.response.InvoiceResponse;
import com.huaducdat.storemanager.service.sales.SalesService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(
            SalesService salesService
    ) {

        this.salesService =
                salesService;
    }

    // =========================
    // CHECKOUT
    // =========================

    @PostMapping("/checkout")
    public BaseResponse<?> checkout(
            HttpServletRequest request,
            @RequestBody CreateInvoiceRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        InvoiceResponse response =
                salesService.checkout(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Checkout success")
                .data(response)
                .build();
    }

    @GetMapping("/history")
    public BaseResponse<?> history(
            HttpServletRequest request
    ) {
        User currentUser =
                CurrentUserUtil.get(
                        request
                );
        return BaseResponse.builder()
                .success(true)
                .message("Invoice history")
                .data(
                        salesService.history(
                                currentUser
                        )
                )
                .build();
    }

    @GetMapping("/{id}")
    public BaseResponse<?> detail(
            @PathVariable Long id
    ) {

        return BaseResponse.builder()
                .success(true)
                .message("Invoice detail")
                .data(
                        salesService.detail(id)
                )
                .build();
    }
}