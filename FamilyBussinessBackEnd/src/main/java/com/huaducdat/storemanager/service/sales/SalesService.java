package com.huaducdat.storemanager.service.sales;

import com.huaducdat.storemanager.model.entity.*;
import com.huaducdat.storemanager.model.request.CreateInvoiceRequest;
import com.huaducdat.storemanager.model.response.InvoiceDetailResponse;
import com.huaducdat.storemanager.model.response.InvoiceItemResponse;
import com.huaducdat.storemanager.model.response.InvoiceResponse;
import com.huaducdat.storemanager.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SalesService {

    private final ProductRepository productRepository;

    private final InvoiceRepository invoiceRepository;

    private final InvoiceItemRepository itemRepository;

    private final CustomerRepository customerRepository;

    public SalesService(
            ProductRepository productRepository,
            InvoiceRepository invoiceRepository,
            InvoiceItemRepository itemRepository, CustomerRepository customerRepository
    ) {

        this.productRepository =
                productRepository;

        this.invoiceRepository =
                invoiceRepository;

        this.itemRepository =
                itemRepository;
        this.customerRepository = customerRepository;
    }

    // =========================
    // CHECKOUT
    // =========================

    public InvoiceResponse checkout(
            User currentUser,
            CreateInvoiceRequest request
    ) {

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
        // STOCK
        // =========================

        if (product.getStockQuantity()
                < request.getQuantity()) {

            throw new RuntimeException(
                    "Not enough stock"
            );
        }

        Customer customer = null;

        if (request.getCustomerId() != null) {

            customer =
                    customerRepository
                            .findById(
                                    request.getCustomerId()
                            )
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Customer not found"
                                    )
                            );
        }


        // =========================
        // CREATE INVOICE
        // =========================

        Invoice invoice =
                new Invoice();

        invoice.setStore(
                currentUser.getStore()
        );

        invoice.setCashier(
                currentUser
        );

        invoice.setCustomer(customer);






        // =========================
        // TOTAL
        // =========================

        double total =
                product.getSellPrice()
                        * request.getQuantity();

        boolean payLater =
                Boolean.TRUE.equals(
                        request.getPayLater()
                );

        invoice.setPaid(
                !payLater
        );

        invoice.setTotalAmount(total);

        invoice.setCompleted(true);

        invoiceRepository.save(invoice);

        if (payLater && customer != null) {

            double currentDebt =
                    customer.getDebtAmount();

            customer.setDebtAmount(
                    currentDebt + total
            );

            customerRepository.save(
                    customer
            );
        }
        // =========================
        // ITEM
        // =========================

        InvoiceItem item =
                new InvoiceItem();

        item.setInvoice(invoice);

        item.setProduct(product);

        item.setProductName(
                product.getName()
        );

        item.setProductPrice(
                product.getSellPrice()
        );

        item.setQuantity(
                request.getQuantity()
        );

        item.setTotalPrice(total);

        itemRepository.save(item);

        // =========================
        // DEDUCT STOCK
        // =========================

        product.setStockQuantity(
                product.getStockQuantity()
                        - request.getQuantity()
        );

        productRepository.save(product);

        return InvoiceResponse.builder()
                .invoiceId(invoice.getId())
                .totalAmount(total)
                .completed(true)
                .build();
    }

    public List<InvoiceResponse> history() {
        return invoiceRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(invoice ->

                        InvoiceResponse.builder()

                                .invoiceId(
                                        invoice.getId()
                                )

                                .cashierName(
                                        invoice.getCashier()
                                                .getUsername()
                                )

                                .totalAmount(
                                        invoice.getTotalAmount()
                                )

                                .completed(
                                        invoice.getCompleted()
                                )

                                .paid(
                                        invoice.getPaid()
                                )

                                .createdAt(
                                        invoice.getCreatedAt()
                                )

                                .build()
                )
                .toList();
    }

    public InvoiceDetailResponse detail(
            Long invoiceId
    ) {

        Invoice invoice =
                invoiceRepository
                        .findById(invoiceId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invoice not found"
                                )
                        );

        List<InvoiceItemResponse> items =
                itemRepository
                        .findByInvoiceId(
                                invoiceId
                        )
                        .stream()
                        .map(item ->

                                InvoiceItemResponse
                                        .builder()

                                        .productName(
                                                item.getProductName()
                                        )

                                        .productPrice(
                                                item.getProductPrice()
                                        )

                                        .quantity(
                                                item.getQuantity()
                                        )

                                        .totalPrice(
                                                item.getTotalPrice()
                                        )

                                        .build()
                        )
                        .toList();

        return InvoiceDetailResponse
                .builder()

                .invoiceId(
                        invoice.getId()
                )

                .cashierName(
                        invoice.getCashier()
                                .getUsername()
                )

                .totalAmount(
                        invoice.getTotalAmount()
                )

                .completed(
                        invoice.getCompleted()
                )

                .paid(
                        invoice.getPaid()
                )

                .createdAt(
                        invoice.getCreatedAt()
                )

                .items(items)

                .build();
    }
}