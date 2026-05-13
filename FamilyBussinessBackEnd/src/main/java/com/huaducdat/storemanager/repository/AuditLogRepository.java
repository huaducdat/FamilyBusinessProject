package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByStoreIdOrderByCreatedAtDesc(
            Long storeId
    );
}