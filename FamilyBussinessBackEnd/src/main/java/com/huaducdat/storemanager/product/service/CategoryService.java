package com.huaducdat.storemanager.product.service;

import com.huaducdat.storemanager.product.model.entity.Category;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.product.dto.request.CreateCategoryRequest;
import com.huaducdat.storemanager.product.dto.response.CategoryResponse;
import com.huaducdat.storemanager.product.repository.CategoryRepository;
import com.huaducdat.storemanager.shared.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(
            CategoryRepository categoryRepository
    ) {

        this.categoryRepository =
                categoryRepository;
    }

    // =========================
    // CREATE
    // =========================

    public Category create(
            User currentUser,
            CreateCategoryRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Store store =
                currentUser.getStore();

        Category category =
                new Category();

        category.setStore(store);

        category.setName(
                request.getName()
        );

        category.setDescription(
                request.getDescription()
        );

        category.setActive(true);

        return categoryRepository
                .save(category);
    }

    // =========================
    // LIST
    // =========================

    public List<CategoryResponse> list(
            User currentUser
    ) {

        return categoryRepository
                .findByStoreId(
                        currentUser.getStore()
                                .getId()
                )
                .stream()
                .map(category ->
                        CategoryResponse.builder()
                                .id(category.getId())
                                .name(
                                        category.getName()
                                )
                                .description(
                                        category.getDescription()
                                )
                                .active(
                                        category.getActive()
                                )
                                .build()
                )
                .toList();
    }
}
