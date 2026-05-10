package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.Product;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateProductRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.product.ProductService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.huaducdat.storemanager.model.request.StockRequest;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {

        this.productService =
                productService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public BaseResponse<?> create(
            HttpServletRequest request,
            @RequestBody CreateProductRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Product product =
                productService.create(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Product created")
                .data(product.getName())
                .build();
    }

    // =========================
    // LIST
    // =========================

    @GetMapping
    public BaseResponse<?> list() {

        return BaseResponse.builder()
                .success(true)
                .message("Product list")
                .data(
                        productService.list()
                )
                .build();
    }

    @PostMapping("/{id}/import")
    public BaseResponse<?> importStock(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody StockRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        productService.importStock(
                currentUser,
                id,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Import stock success")
                .data(null)
                .build();
    }

    @PostMapping("/{id}/export")
    public BaseResponse<?> exportStock(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody StockRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        productService.exportStock(
                currentUser,
                id,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Export stock success")
                .data(null)
                .build();
    }

    @GetMapping("/{id}/inventory-history")
    public BaseResponse<?> inventoryHistory(
            @PathVariable Long id
    ) {

        return BaseResponse.builder()
                .success(true)
                .message("Inventory history")
                .data(
                        productService
                                .inventoryHistory(id)
                )
                .build();
    }
}