package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository
        extends JpaRepository<Supplier, Long> {
}