package com.huaducdat.storemanager.purchase.repository;

import com.huaducdat.storemanager.purchase.model.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderItemRepository
        extends JpaRepository<PurchaseOrderItem, Long> {
}
