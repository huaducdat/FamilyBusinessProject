package com.huaducdat.storemanager.payroll.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "salary_configs")
@Getter
@Setter
public class SalaryConfig
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // USER
    // =========================

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // =========================
    // SALARY
    // =========================

    private Double hourlyRate;

    private Double overtimeRate;

    private Double bonusAmount;

    private Double deductionAmount;
}
