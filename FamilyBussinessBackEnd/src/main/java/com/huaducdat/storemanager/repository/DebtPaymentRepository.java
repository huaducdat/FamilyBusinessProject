package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.DebtPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtPaymentRepository
        extends JpaRepository<DebtPayment, Long> {
}