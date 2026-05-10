package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {
}