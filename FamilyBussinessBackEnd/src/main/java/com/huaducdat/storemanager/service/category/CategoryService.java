package com.huaducdat.storemanager.service.category;

import com.huaducdat.storemanager.model.entity.Category;
import com.huaducdat.storemanager.model.entity.Store;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.CreateCategoryRequest;
import com.huaducdat.storemanager.model.response.CategoryResponse;
import com.huaducdat.storemanager.repository.CategoryRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
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

    public List<CategoryResponse> list() {

        return categoryRepository
                .findAll()
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