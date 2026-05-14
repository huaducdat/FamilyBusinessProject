package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.SalaryConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryConfigRepository
        extends JpaRepository<SalaryConfig, Long> {

    Optional<SalaryConfig>
    findByUserId(
            Long userId
    );
}