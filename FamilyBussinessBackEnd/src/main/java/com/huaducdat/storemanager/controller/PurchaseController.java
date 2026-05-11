package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreatePurchaseRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.model.response.PurchaseResponse;
import com.huaducdat.storemanager.service.purchase.PurchaseService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(
            PurchaseService purchaseService
    ) {

        this.purchaseService =
                purchaseService;
    }

    // =========================
    // CREATE PURCHASE
    // =========================

    @PostMapping
    public BaseResponse<?> purchase(
            HttpServletRequest request,
            @RequestBody CreatePurchaseRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        PurchaseResponse response =
                purchaseService.purchase(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Purchase success")
                .data(response)
                .build();
    }
}