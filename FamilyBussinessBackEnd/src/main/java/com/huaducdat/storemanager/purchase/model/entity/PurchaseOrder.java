package com.huaducdat.storemanager.purchase.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.supplier.model.entity.Supplier;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
public class PurchaseOrder
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // STORE
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    // =========================
    // SUPPLIER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    // =========================
    // CREATOR
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // =========================
    // INFO
    // =========================

    private Double totalAmount = 0.0;

    private Boolean completed = false;
}
