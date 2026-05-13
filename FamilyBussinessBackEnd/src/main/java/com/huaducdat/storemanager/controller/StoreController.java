package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateStoreRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.store.StoreService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(
            StoreService storeService
    ) {

        this.storeService =
                storeService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public BaseResponse<?> create(
            HttpServletRequest request,
            @RequestBody CreateStoreRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Store store =
                storeService.create(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Store created")
                .data(
                        store.getName()
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
                .message("Store list")
                .data(
                        storeService.list()
                )
                .build();
    }
}