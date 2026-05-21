package com.huaducdat.storemanager.product.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends BaseEntity {

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
    // CATEGORY
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    // =========================
    // INFO
    // =========================

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String barcode;

    private String description;

    // =========================
    // PRICE
    // =========================

    private Double importPrice;

    private Double sellPrice;

    // =========================
    // STOCK
    // =========================

    private Integer stockQuantity = 0;

    // =========================
    // STATUS
    // =========================

    private Boolean active = true;
}
