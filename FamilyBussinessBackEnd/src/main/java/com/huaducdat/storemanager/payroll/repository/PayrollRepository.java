package com.huaducdat.storemanager.payroll.repository;

import com.huaducdat.storemanager.payroll.model.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRepository
        extends JpaRepository<Payroll, Long> {

    List<Payroll>
    findByPayrollMonthAndPayrollYear(
            Integer month,
            Integer year
    );

    List<Payroll> findByUserIdAndUserStoreId(
            Long userId,
            Long storeId
    );

    List<Payroll> findByUserStoreIdAndPayrollMonthAndPayrollYear(
            Long storeId,
            Integer month,
            Integer year
    );
}
