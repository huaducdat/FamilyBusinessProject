package com.huaducdat.storemanager.sales.repository;

import com.huaducdat.storemanager.sales.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByOrderByCreatedAtDesc();
    List<Invoice> findByStoreIdAndCreatedAtBetween(
            Long storeId,
            LocalDateTime start,
            LocalDateTime end
    );
    List<Invoice> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
    Optional<Invoice> findByIdAndStoreId(
            Long id,
            Long storeId
    );
    List<Invoice> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );
    List<Invoice> findByCustomerIdAndStoreIdOrderByCreatedAtDesc(
            Long customerId,
            Long storeId
    );
    List<Invoice> findByStoreIdOrderByCreatedAtDesc(
            Long storeId
    );
}
