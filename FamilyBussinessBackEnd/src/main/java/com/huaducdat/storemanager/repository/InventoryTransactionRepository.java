package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction>
    findByProductIdOrderByCreatedAtDesc(
            Long productId
    );
}