package com.huaducdat.storemanager.report.service;

import com.huaducdat.storemanager.sales.model.entity.Invoice;
import com.huaducdat.storemanager.sales.model.entity.InvoiceItem;
import com.huaducdat.storemanager.product.model.entity.Product;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.dashboard.dto.response.BestSellerResponse;
import com.huaducdat.storemanager.report.dto.response.MonthlyRevenueReportResponse;
import com.huaducdat.storemanager.report.dto.response.ProfitReportResponse;
import com.huaducdat.storemanager.sales.repository.InvoiceItemRepository;
import com.huaducdat.storemanager.sales.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    // TODO: future full tenant isolation; these report guards are lightweight and store-scoped only.

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository itemRepository;

    public ReportService(
            InvoiceRepository invoiceRepository, InvoiceItemRepository itemRepository
    ) {

        this.invoiceRepository =
                invoiceRepository;
        this.itemRepository = itemRepository;
    }

    // =========================
    // MONTHLY REVENUE
    // =========================

    public MonthlyRevenueReportResponse
    monthlyRevenue(
            User currentUser
    ) {

        LocalDate now =
                LocalDate.now();

        LocalDateTime start =
                now.withDayOfMonth(1)
                        .atStartOfDay();

        LocalDateTime end =
                now.withDayOfMonth(
                                now.lengthOfMonth()
                        )
                        .atTime(
                                23,
                                59,
                                59
                        );

        List<Invoice> invoices =
                invoiceRepository
                        .findByStoreIdAndCreatedAtBetween(
                                currentUser.getStore()
                                        .getId(),
                                start,
                                end
                        );

        int totalInvoices =
                invoices.size();

        double totalRevenue =
                invoices.stream()
                        .map(Invoice::getTotalAmount)
                        .reduce(
                                0.0,
                                Double::sum
                        );

        double average =
                totalInvoices == 0
                        ? 0
                        : totalRevenue
                        / totalInvoices;

        return MonthlyRevenueReportResponse
                .builder()

                .totalInvoices(
                        totalInvoices
                )

                .totalRevenue(
                        totalRevenue
                )

                .averageInvoiceValue(
                        average
                )

                .build();
    }

    public List<BestSellerResponse>
    bestSeller(
            User currentUser
    ) {

        List<InvoiceItem> items =
                itemRepository.findByInvoiceStoreId(
                        currentUser.getStore()
                                .getId()
                );

        Map<String, List<InvoiceItem>> grouped =
                items.stream()
                        .collect(
                                Collectors.groupingBy(
                                        InvoiceItem::getProductName
                                )
                        );

        return grouped.entrySet()
                .stream()

                .map(entry -> {

                    String productName =
                            entry.getKey();

                    List<InvoiceItem> productItems =
                            entry.getValue();

                    int totalQuantity =
                            productItems.stream()
                                    .map(
                                            InvoiceItem::getQuantity
                                    )
                                    .reduce(
                                            0,
                                            Integer::sum
                                    );

                    double totalRevenue =
                            productItems.stream()
                                    .map(
                                            InvoiceItem::getTotalPrice
                                    )
                                    .reduce(
                                            0.0,
                                            Double::sum
                                    );

                    return BestSellerResponse
                            .builder()

                            .productName(
                                    productName
                            )

                            .totalQuantity(
                                    totalQuantity
                            )

                            .totalRevenue(
                                    totalRevenue
                            )

                            .build();
                })

                // =========================
                // SORT DESC
                // =========================

                .sorted((a, b) ->

                        Integer.compare(
                                b.getTotalQuantity(),
                                a.getTotalQuantity()
                        )
                )

                .toList();
    }

    public ProfitReportResponse
    profitReport(
            User currentUser
    ) {

        List<InvoiceItem> items =
                itemRepository.findByInvoiceStoreId(
                        currentUser.getStore()
                                .getId()
                );

        double revenue = 0;

        double cost = 0;

        for (InvoiceItem item : items) {

            revenue +=
                    item.getTotalPrice();

            Product product =
                    item.getProduct();

            double itemCost =
                    product.getImportPrice()
                            * item.getQuantity();

            cost += itemCost;
        }

        double profit =
                revenue - cost;

        double margin =
                revenue == 0
                        ? 0
                        : (profit / revenue) * 100;

        return ProfitReportResponse
                .builder()

                .totalRevenue(revenue)

                .estimatedCost(cost)

                .estimatedProfit(profit)

                .marginPercent(margin)

                .build();
    }
}
