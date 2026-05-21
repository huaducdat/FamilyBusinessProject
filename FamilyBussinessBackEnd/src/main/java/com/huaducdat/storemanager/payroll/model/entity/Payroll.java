package com.huaducdat.storemanager.payroll.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payrolls")
@Getter
@Setter
public class Payroll
        extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // USER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // =========================
    // MONTH
    // =========================

    private Integer payrollMonth;

    private Integer payrollYear;

    // =========================
    // WORK
    // =========================

    private Double totalHours;

    private Double overtimeHours;

    // =========================
    // MONEY
    // =========================

    private Double baseSalary;

    private Double overtimeSalary;

    private Double bonusAmount;

    private Double deductionAmount;

    private Double finalSalary;
}
