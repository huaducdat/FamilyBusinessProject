package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceItemRepository
        extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoiceId(
            Long invoiceId
    );

    List<InvoiceItem> findAll();
}