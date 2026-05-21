package com.huaducdat.storemanager.customer.repository;

import com.huaducdat.storemanager.customer.model.entity.DebtPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtPaymentRepository
        extends JpaRepository<DebtPayment, Long> {
}
