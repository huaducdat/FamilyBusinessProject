package com.huaducdat.storemanager.supplier.controller;

import com.huaducdat.storemanager.supplier.model.entity.Supplier;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.supplier.dto.request.CreateSupplierRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.supplier.service.SupplierService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
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
    public BaseResponse<?> list(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Supplier list")
                .data(
                        supplierService.list(
                                currentUser
                        )
                )
                .build();
    }
}
