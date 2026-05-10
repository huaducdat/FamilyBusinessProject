package com.huaducdat.storemanager.service.product;

import com.huaducdat.storemanager.model.entity.*;
import com.huaducdat.storemanager.model.enumtype.InventoryType;
import com.huaducdat.storemanager.model.request.CreateProductRequest;
import com.huaducdat.storemanager.model.request.StockRequest;
import com.huaducdat.storemanager.model.response.InventoryHistoryResponse;
import com.huaducdat.storemanager.model.response.ProductResponse;
import com.huaducdat.storemanager.repository.CategoryRepository;
import com.huaducdat.storemanager.repository.InventoryTransactionRepository;
import com.huaducdat.storemanager.repository.ProductRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final InventoryTransactionRepository inventoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository, InventoryTransactionRepository inventoryRepository
    ) {

        this.productRepository =
                productRepository;

        this.categoryRepository =
                categoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // =========================
    // CREATE
    // =========================

    public Product create(
            User currentUser,
            CreateProductRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Category category =
                categoryRepository
                        .findById(
                                request.getCategoryId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Category not found"
                                )
                        );

        Store store =
                currentUser.getStore();

        Product product =
                new Product();

        product.setStore(store);

        product.setCategory(category);

        product.setName(
                request.getName()
        );

        product.setBarcode(
                request.getBarcode()
        );

        product.setDescription(
                request.getDescription()
        );

        product.setImportPrice(
                request.getImportPrice()
        );

        product.setSellPrice(
                request.getSellPrice()
        );

        product.setStockQuantity(
                request.getStockQuantity()
        );

        product.setActive(true);

        return productRepository.save(product);
    }

    // =========================
    // LIST
    // =========================

    public List<ProductResponse> list() {

        return productRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // RESPONSE
    // =========================

    private ProductResponse toResponse(
            Product product
    ) {

        return ProductResponse.builder()
                .id(product.getId())
                .categoryName(
                        product.getCategory()
                                .getName()
                )
                .name(product.getName())
                .barcode(product.getBarcode())
                .description(
                        product.getDescription()
                )
                .importPrice(
                        product.getImportPrice()
                )
                .sellPrice(
                        product.getSellPrice()
                )
                .stockQuantity(
                        product.getStockQuantity()
                )
                .active(
                        product.getActive()
                )
                .build();
    }

    public void importStock(
            User currentUser,
            Long productId,
            StockRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found"
                                )
                        );

        int before =
                product.getStockQuantity();

        int after =
                before + request.getQuantity();

        // =========================
        // UPDATE STOCK
        // =========================

        product.setStockQuantity(after);

        productRepository.save(product);

        // =========================
        // HISTORY
        // =========================

        InventoryTransaction transaction =
                new InventoryTransaction();

        transaction.setProduct(product);

        transaction.setUser(currentUser);

        transaction.setType(
                InventoryType.IMPORT
        );

        transaction.setQuantity(
                request.getQuantity()
        );

        transaction.setBeforeQuantity(
                before
        );

        transaction.setAfterQuantity(
                after
        );

        transaction.setNote(
                request.getNote()
        );

        inventoryRepository.save(
                transaction
        );
    }

    public void exportStock(
            User currentUser,
            Long productId,
            StockRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Product not found"
                                )
                        );

        int before =
                product.getStockQuantity();

        int after =
                before - request.getQuantity();

        if (after < 0) {

            throw new RuntimeException(
                    "Not enough stock"
            );
        }

        // =========================
        // UPDATE STOCK
        // =========================

        product.setStockQuantity(after);

        productRepository.save(product);

        // =========================
        // HISTORY
        // =========================

        InventoryTransaction transaction =
                new InventoryTransaction();

        transaction.setProduct(product);

        transaction.setUser(currentUser);

        transaction.setType(
                InventoryType.EXPORT
        );

        transaction.setQuantity(
                request.getQuantity()
        );

        transaction.setBeforeQuantity(
                before
        );

        transaction.setAfterQuantity(
                after
        );

        transaction.setNote(
                request.getNote()
        );

        inventoryRepository.save(
                transaction
        );
    }


    public List<InventoryHistoryResponse>
    inventoryHistory(
            Long productId
    ) {

        return inventoryRepository
                .findByProductIdOrderByCreatedAtDesc(
                        productId
                )
                .stream()
                .map(transaction ->

                        InventoryHistoryResponse
                                .builder()

                                .id(
                                        transaction.getId()
                                )

                                .username(
                                        transaction.getUser()
                                                .getUsername()
                                )

                                .type(
                                        transaction.getType()
                                )

                                .quantity(
                                        transaction.getQuantity()
                                )

                                .beforeQuantity(
                                        transaction
                                                .getBeforeQuantity()
                                )

                                .afterQuantity(
                                        transaction
                                                .getAfterQuantity()
                                )

                                .note(
                                        transaction.getNote()
                                )

                                .createdAt(
                                        transaction.getCreatedAt()
                                )

                                .build()
                )
                .toList();
    }
}