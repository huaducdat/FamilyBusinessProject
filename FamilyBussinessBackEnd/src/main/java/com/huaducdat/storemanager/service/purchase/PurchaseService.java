package com.huaducdat.storemanager.service.purchase;

import com.huaducdat.storemanager.model.entity.*;
import com.huaducdat.storemanager.model.enumtype.InventoryType;
import com.huaducdat.storemanager.model.request.CreatePurchaseRequest;
import com.huaducdat.storemanager.model.response.PurchaseResponse;
import com.huaducdat.storemanager.repository.*;
import com.huaducdat.storemanager.service.util.PermissionUtil;
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

    public PurchaseService(
            SupplierRepository supplierRepository,
            ProductRepository productRepository,
            PurchaseOrderRepository purchaseRepository,
            PurchaseOrderItemRepository itemRepository,
            InventoryTransactionRepository inventoryRepository
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
                        .findById(
                                request.getSupplierId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Supplier not found"
                                )
                        );

        Product product =
                productRepository
                        .findById(
                                request.getProductId()
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