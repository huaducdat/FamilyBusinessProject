package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByOrderByCreatedAtDesc();
    List<Invoice> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
    List<Invoice> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );
}