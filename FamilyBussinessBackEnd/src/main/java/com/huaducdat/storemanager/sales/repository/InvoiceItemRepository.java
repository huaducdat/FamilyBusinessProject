package com.huaducdat.storemanager.sales.repository;

import com.huaducdat.storemanager.sales.model.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceItemRepository
        extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoiceId(
            Long invoiceId
    );

    List<InvoiceItem> findByInvoiceIdAndInvoiceStoreId(
            Long invoiceId,
            Long storeId
    );

    List<InvoiceItem> findByInvoiceStoreId(
            Long storeId
    );

    List<InvoiceItem> findByProductIdOrderByCreatedAtDesc(
            Long productId
    );

    List<InvoiceItem> findByProductIdAndInvoiceStoreIdOrderByCreatedAtDesc(
            Long productId,
            Long storeId
    );
}
