package com.huaducdat.storemanager.product.repository;

import com.huaducdat.storemanager.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
    List<Product> findByStockQuantityLessThan(
            Integer quantity
    );

    List<Product> findByStoreId(
            Long storeId
    );

    Optional<Product> findByIdAndStoreId(
            Long id,
            Long storeId
    );
}
