package com.huaducdat.storemanager.product.controller;

import com.huaducdat.storemanager.product.model.entity.Product;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.product.dto.request.CreateProductRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.product.service.ProductService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.huaducdat.storemanager.product.dto.request.StockRequest;


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
    public BaseResponse<?> list(
            HttpServletRequest request
    ){
        User currentUser =
                CurrentUserUtil.get(
                        request
                );
        return BaseResponse.builder()
                .success(true)
                .message("Product list")
                .data(
                        productService.list(
                                currentUser
                        )
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
            HttpServletRequest request,
            @PathVariable Long id
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Inventory history")
                .data(
                        productService
                                .inventoryHistory(
                                        currentUser,
                                        id
                                )
                )
                .build();
    }
}
