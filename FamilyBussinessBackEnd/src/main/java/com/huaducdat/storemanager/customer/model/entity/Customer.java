package com.huaducdat.storemanager.customer.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer extends BaseEntity {

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
    // INFO
    // =========================

    private String fullName;

    private String phone;

    private String address;

    // =========================
    // DEBT
    // =========================

    private Double debtAmount = 0.0;

    // =========================
    // STATUS
    // =========================

    private Boolean active = true;
}
