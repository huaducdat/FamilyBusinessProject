package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRepository
        extends JpaRepository<Payroll, Long> {

    List<Payroll>
    findByPayrollMonthAndPayrollYear(
            Integer month,
            Integer year
    );
}