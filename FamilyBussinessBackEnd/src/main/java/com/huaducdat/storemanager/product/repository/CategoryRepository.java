package com.huaducdat.storemanager.product.repository;

import com.huaducdat.storemanager.product.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    List<Category> findByStoreId(
            Long storeId
    );

    Optional<Category> findByIdAndStoreId(
            Long id,
            Long storeId
    );
}
