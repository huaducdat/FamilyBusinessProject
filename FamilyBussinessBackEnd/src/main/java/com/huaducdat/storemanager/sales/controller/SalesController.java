package com.huaducdat.storemanager.sales.controller;

import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.sales.dto.request.CreateInvoiceRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.sales.dto.response.InvoiceResponse;
import com.huaducdat.storemanager.sales.service.SalesService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
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
            HttpServletRequest request,
            @PathVariable Long id
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Invoice detail")
                .data(
                        salesService.detail(
                                currentUser,
                                id
                        )
                )
                .build();
    }
}
