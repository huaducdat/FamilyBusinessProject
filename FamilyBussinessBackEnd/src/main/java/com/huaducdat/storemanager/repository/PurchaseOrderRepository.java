package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {
}