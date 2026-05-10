package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
}