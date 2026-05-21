package com.huaducdat.storemanager.purchase.service;

import com.huaducdat.storemanager.product.model.entity.InventoryTransaction;
import com.huaducdat.storemanager.product.model.entity.Product;
import com.huaducdat.storemanager.product.repository.InventoryTransactionRepository;
import com.huaducdat.storemanager.product.repository.ProductRepository;
import com.huaducdat.storemanager.purchase.model.entity.PurchaseOrder;
import com.huaducdat.storemanager.purchase.model.entity.PurchaseOrderItem;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.supplier.model.entity.Supplier;
import com.huaducdat.storemanager.supplier.repository.SupplierRepository;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.model.enumtype.InventoryType;
import com.huaducdat.storemanager.purchase.dto.request.CreatePurchaseRequest;
import com.huaducdat.storemanager.purchase.dto.response.PurchaseResponse;
import com.huaducdat.storemanager.audit.service.AuditService;
import com.huaducdat.storemanager.purchase.repository.*;
import com.huaducdat.storemanager.shared.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PurchaseService {

    private final SupplierRepository supplierRepository;

    private final ProductRepository productRepository;

    private final PurchaseOrderRepository purchaseRepository;

    private final PurchaseOrderItemRepository itemRepository;

    private final InventoryTransactionRepository inventoryRepository;

    private final AuditService auditService;

    public PurchaseService(
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            PurchaseOrderRepository purchaseRepository,
            PurchaseOrderItemRepository itemRepository,
            InventoryTransactionRepository inventoryRepository, AuditService auditService
    ) {

        this.supplierRepository =
                supplierRepository;

        this.productRepository =
                productRepository;

        this.purchaseRepository =
                purchaseRepository;

        this.itemRepository =
                itemRepository;

        this.inventoryRepository =
                inventoryRepository;
        this.auditService = auditService;
    }

    // =========================
    // PURCHASE
    // =========================

    public PurchaseResponse purchase(
            User currentUser,
            CreatePurchaseRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Supplier supplier =
                supplierRepository
                        .findByIdAndStoreId(
                                request.getSupplierId()
                                ,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Supplier not found"
                                )
                        );

        Product product =
                productRepository
                        .findByIdAndStoreId(
                                request.getProductId()
                                ,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found"
                                )
                        );

        // =========================
        // TOTAL
        // =========================

        double total =
                product.getImportPrice()
                        * request.getQuantity();

        // =========================
        // ORDER
        // =========================

        PurchaseOrder order =
                new PurchaseOrder();

        order.setStore(
                currentUser.getStore()
        );

        order.setSupplier(
                supplier
        );

        order.setCreatedBy(
                currentUser
        );

        order.setTotalAmount(total);

        order.setCompleted(true);

        purchaseRepository.save(order);

        // =========================
        // ITEM
        // =========================

        PurchaseOrderItem item =
                new PurchaseOrderItem();

        item.setPurchaseOrder(order);

        item.setProduct(product);

        item.setProductName(
                product.getName()
        );

        item.setImportPrice(
                product.getImportPrice()
        );

        item.setQuantity(
                request.getQuantity()
        );

        item.setTotalPrice(total);

        itemRepository.save(item);

        // =========================
        // STOCK
        // =========================

        int before =
                product.getStockQuantity();

        int after =
                before + request.getQuantity();

        product.setStockQuantity(after);

        productRepository.save(product);

        // =========================
        // INVENTORY HISTORY
        // =========================

        InventoryTransaction tx =
                new InventoryTransaction();

        tx.setProduct(product);

        tx.setUser(currentUser);

        tx.setType(
                InventoryType.IMPORT
        );

        tx.setQuantity(
                request.getQuantity()
        );

        tx.setBeforeQuantity(before);

        tx.setAfterQuantity(after);

        tx.setNote(
                "Purchase Order #" + order.getId()
        );

        inventoryRepository.save(tx);

        auditService.log(
                currentUser,
                AuditAction.PURCHASE,
                "Purchase order #"
                        + order.getId()
        );

        return PurchaseResponse.builder()
                .purchaseOrderId(
                        order.getId()
                )
                .supplierName(
                        supplier.getName()
                )
                .totalAmount(total)
                .completed(true)
                .build();
    }
}
