package com.huaducdat.storemanager.service.dashboard;

import com.huaducdat.storemanager.model.entity.Invoice;
import com.huaducdat.storemanager.model.response.DashboardResponse;
import com.huaducdat.storemanager.repository.AttendanceRepository;
import com.huaducdat.storemanager.repository.InvoiceRepository;
import com.huaducdat.storemanager.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final InvoiceRepository invoiceRepository;

    private final ProductRepository productRepository;

    private final AttendanceRepository attendanceRepository;

    public DashboardService(
            InvoiceRepository invoiceRepository,
            ProductRepository productRepository,
            AttendanceRepository attendanceRepository
    ) {

        this.invoiceRepository =
                invoiceRepository;

        this.productRepository =
                productRepository;

        this.attendanceRepository =
                attendanceRepository;
    }

    public DashboardResponse dashboard() {

        // =========================
        // TODAY RANGE
        // =========================

        LocalDate today =
                LocalDate.now();

        LocalDateTime start =
                today.atStartOfDay();

        LocalDateTime end =
                today.atTime(
                        23,
                        59,
                        59
                );

        // =========================
        // TODAY INVOICES
        // =========================

        List<Invoice> invoices =
                invoiceRepository
                        .findByCreatedAtBetween(
                                start,
                                end
                        );

        double revenue =
                invoices.stream()
                        .map(Invoice::getTotalAmount)
                        .reduce(
                                0.0,
                                Double::sum
                        );

        int invoiceCount =
                invoices.size();

        // =========================
        // LOW STOCK
        // =========================

        int lowStock =
                productRepository
                        .findByStockQuantityLessThan(
                                10
                        )
                        .size();

        // =========================
        // ATTENDANCE
        // =========================

        int attendance =
                attendanceRepository
                        .findByWorkDate(today)
                        .size();

        return DashboardResponse
                .builder()

                .todayRevenue(revenue)

                .todayInvoiceCount(
                        invoiceCount
                )

                .lowStockCount(
                        lowStock
                )

                .attendanceTodayCount(
                        attendance
                )

                .build();
    }
}