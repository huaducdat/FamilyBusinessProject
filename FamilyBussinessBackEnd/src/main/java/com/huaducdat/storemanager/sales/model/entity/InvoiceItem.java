package com.huaducdat.storemanager.sales.model.entity;

import com.huaducdat.storemanager.product.model.entity.Product;
import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_items")
@Getter
@Setter
public class InvoiceItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // INVOICE
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

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

    private Double productPrice;

    // =========================
    // QUANTITY
    // =========================

    private Integer quantity;

    // =========================
    // TOTAL
    // =========================

    private Double totalPrice;
}
