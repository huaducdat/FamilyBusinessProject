package com.huaducdat.storemanager.model.entity;

import com.huaducdat.storemanager.model.enumtype.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // STORE
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // =========================
    // LOGIN
    // =========================

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // =========================
    // INFO
    // =========================

    private String fullName;

    private String phone;

    // =========================
    // ROLE
    // =========================

    @Enumerated(EnumType.STRING)
    private Role role;

    // =========================
    // STATUS
    // =========================

    private Boolean active = true;
}