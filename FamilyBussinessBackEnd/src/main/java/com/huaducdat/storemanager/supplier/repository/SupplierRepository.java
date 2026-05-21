package com.huaducdat.storemanager.supplier.repository;

import com.huaducdat.storemanager.supplier.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository
        extends JpaRepository<Supplier, Long> {
    List<Supplier> findByStoreId(
            Long storeId
    );

    Optional<Supplier> findByIdAndStoreId(
            Long id,
            Long storeId
    );
}
