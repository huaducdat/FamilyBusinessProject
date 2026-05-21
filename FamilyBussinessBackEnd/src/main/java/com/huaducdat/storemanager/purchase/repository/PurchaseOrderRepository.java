package com.huaducdat.storemanager.purchase.repository;

import com.huaducdat.storemanager.purchase.model.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {
}
