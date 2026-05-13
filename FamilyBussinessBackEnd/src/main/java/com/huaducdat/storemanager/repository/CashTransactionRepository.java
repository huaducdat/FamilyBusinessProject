package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.CashTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CashTransactionRepository
        extends JpaRepository<CashTransaction, Long> {

    List<CashTransaction>
    findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}