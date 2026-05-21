package com.huaducdat.storemanager.product.repository;

import com.huaducdat.storemanager.product.model.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction>
    findByProductIdOrderByCreatedAtDesc(
            Long productId
    );

    List<InventoryTransaction>
    findByProductIdAndProductStoreIdOrderByCreatedAtDesc(
            Long productId,
            Long storeId
    );
}
