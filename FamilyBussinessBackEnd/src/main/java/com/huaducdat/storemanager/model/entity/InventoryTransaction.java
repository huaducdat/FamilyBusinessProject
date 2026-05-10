package com.huaducdat.storemanager.model.entity;

import com.huaducdat.storemanager.model.enumtype.InventoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory_transactions")
@Getter
@Setter
public class InventoryTransaction
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // PRODUCT
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // =========================
    // USER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // =========================
    // TYPE
    // =========================

    @Enumerated(EnumType.STRING)
    private InventoryType type;

    // =========================
    // QUANTITY
    // =========================

    private Integer quantity;

    // =========================
    // STOCK SNAPSHOT
    // =========================

    private Integer beforeQuantity;

    private Integer afterQuantity;

    // =========================
    // NOTE
    // =========================

    private String note;
}