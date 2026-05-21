package com.huaducdat.storemanager.purchase.model.entity;

import com.huaducdat.storemanager.product.model.entity.Product;
import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_order_items")
@Getter
@Setter
public class PurchaseOrderItem
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // ORDER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    // =========================
    // PRODUCT
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // =========================
    // SNAPSHOT
    // =========================

    private String productName;

    private Double importPrice;

    // =========================
    // QUANTITY
    // =========================

    private Integer quantity;

    // =========================
    // TOTAL
    // =========================

    private Double totalPrice;
}
