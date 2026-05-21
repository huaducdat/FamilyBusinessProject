package com.huaducdat.storemanager.audit.model.entity;

import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog
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
    @JoinColumn(name = "user_id")
    private User user;

    // =========================
    // ACTION
    // =========================

    @Enumerated(EnumType.STRING)
    private AuditAction action;

    // =========================
    // DESCRIPTION
    // =========================

    @Column(length = 2000)
    private String description;
}
