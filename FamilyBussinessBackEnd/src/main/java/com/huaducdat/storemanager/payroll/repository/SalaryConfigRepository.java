package com.huaducdat.storemanager.payroll.repository;

import com.huaducdat.storemanager.payroll.model.entity.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryConfigRepository
        extends JpaRepository<SalaryConfig, Long> {

    Optional<SalaryConfig>
    findByUserId(
            Long userId
    );

    Optional<SalaryConfig> findByUserIdAndUserStoreId(
            Long userId,
            Long storeId
    );
}
