package com.huaducdat.storemanager.cashflow.model.entity;

import com.huaducdat.storemanager.model.enumtype.CashTransactionType;
import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cash_transactions")
@Getter
@Setter
public class CashTransaction
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
    // USER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // =========================
    // TYPE
    // =========================

    @Enumerated(EnumType.STRING)
    private CashTransactionType type;

    // =========================
    // MONEY
    // =========================

    private Double amount;

    // =========================
    // NOTE
    // =========================

    private String note;
}
