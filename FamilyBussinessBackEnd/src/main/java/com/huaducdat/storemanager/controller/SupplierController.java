package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.Supplier;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateSupplierRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.supplier.SupplierService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(
            SupplierService supplierService
    ) {

        this.supplierService =
                supplierService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public BaseResponse<?> create(
            HttpServletRequest request,
            @RequestBody CreateSupplierRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Supplier supplier =
                supplierService.create(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Supplier created")
                .data(
                        supplier.getName()
                )
                .build();
    }

    // =========================
    // LIST
    // =========================

    @GetMapping
    public BaseResponse<?> list() {

        return BaseResponse.builder()
                .success(true)
                .message("Supplier list")
                .data(
                        supplierService.list()
                )
                .build();
    }
}