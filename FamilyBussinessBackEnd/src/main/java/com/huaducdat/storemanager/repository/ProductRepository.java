package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
    List<Product> findByStockQuantityLessThan(
            Integer quantity
    );

    List<Product> findByStoreId(
            Long storeId
    );
}