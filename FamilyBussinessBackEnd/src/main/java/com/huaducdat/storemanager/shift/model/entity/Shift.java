package com.huaducdat.storemanager.shift.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "shifts")
@Getter
@Setter
public class Shift
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
    // INFO
    // =========================

    private String name;

    private LocalTime startTime;

    private LocalTime endTime;
}
