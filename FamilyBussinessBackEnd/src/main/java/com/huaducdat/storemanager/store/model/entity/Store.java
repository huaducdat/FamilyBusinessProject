package com.huaducdat.storemanager.store.model.entity;

import com.huaducdat.storemanager.auth.model.entity.Owner;
import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stores")
@Getter
@Setter
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // OWNER
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phone;

    private Boolean active = true;
}
