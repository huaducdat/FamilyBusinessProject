package com.huaducdat.storemanager.audit.repository;

import com.huaducdat.storemanager.audit.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByStoreIdOrderByCreatedAtDesc(
            Long storeId
    );
}
