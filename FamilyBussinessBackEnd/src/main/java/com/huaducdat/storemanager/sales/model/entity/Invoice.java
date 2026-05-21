package com.huaducdat.storemanager.sales.model.entity;

import com.huaducdat.storemanager.customer.model.entity.Customer;
import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice extends BaseEntity {

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
    // CASHIER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cashier_id")
    private User cashier;

    // =========================
    // INFO
    // =========================

    private Double totalAmount = 0.0;

    private Boolean completed = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Boolean paid = true;
}
