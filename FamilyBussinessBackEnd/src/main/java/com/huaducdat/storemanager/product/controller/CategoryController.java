package com.huaducdat.storemanager.product.controller;

import com.huaducdat.storemanager.product.model.entity.Category;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.product.dto.request.CreateCategoryRequest;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.product.service.CategoryService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService
    ) {

        this.categoryService =
                categoryService;
    }

    // =========================
    // CREATE
    // =========================

    @PostMapping
    public BaseResponse<?> create(
            HttpServletRequest request,
            @RequestBody CreateCategoryRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Category category =
                categoryService.create(
                        currentUser,
                        body
                );

        return BaseResponse.builder()
                .success(true)
                .message("Category created")
                .data(category.getName())
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
                .message("Category list")
                .data(
                        categoryService.list(
                                currentUser
                        )
                )
                .build();
    }
}
