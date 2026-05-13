package com.huaducdat.storemanager.service.dashboard;

import com.huaducdat.storemanager.model.entity.*;
import com.huaducdat.storemanager.model.enumtype.CashTransactionType;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.model.response.DashboardResponse;
import com.huaducdat.storemanager.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final InvoiceRepository invoiceRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final CashTransactionRepository cashRepository;

    private final UserRepository userRepository;

    private final AttendanceRepository attendanceRepository;

    public DashboardService(
            InvoiceRepository invoiceRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            CashTransactionRepository cashRepository, UserRepository userRepository, AttendanceRepository attendanceRepository
    ) {

        this.invoiceRepository =
                invoiceRepository;

        this.customerRepository =
                customerRepository;

        this.productRepository =
                productRepository;

        this.cashRepository =
                cashRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public DashboardResponse dashboard(
            User currentUser
    ) {

        Long storeId =
                currentUser
                        .getStore()
                        .getId();

        // =========================
        // TODAY
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
        // INVOICES
        // =========================

        List<Invoice> invoices =
                invoiceRepository
                        .findByStoreIdOrderByCreatedAtDesc(
                                storeId
                        );

        double revenue = 0;

        int todayInvoices = 0;

        for (Invoice invoice : invoices) {

            if (
                    invoice.getCreatedAt()
                            .isAfter(start)
            ) {

                revenue +=
                        invoice.getTotalAmount();

                todayInvoices++;
            }
        }

        // =========================
        // CUSTOMERS
        // =========================

        List<Customer> customers =
                customerRepository
                        .findByStoreId(
                                storeId
                        );

        double totalDebt = 0;

        for (Customer customer : customers) {

            totalDebt +=
                    customer.getDebtAmount();
        }

        // =========================
        // PRODUCTS
        // =========================

        List<Product> products =
                productRepository
                        .findByStoreId(
                                storeId
                        );

        int lowStock = 0;

        for (Product product : products) {

            if (
                    product.getStockQuantity()
                            <= 5
            ) {

                lowStock++;
            }
        }

        // =========================
// EMPLOYEES
// =========================

        List<User> employees =
                userRepository
                        .findByStoreId(
                                storeId
                        );

        int totalEmployees = 0;

        for (User user : employees) {

            if (
                    user.getUserRole()
                            != UserRole.OWNER
                            &&
                            user.getUserRole()
                                    != UserRole.ADMIN
            ) {

                totalEmployees++;
            }
        }

        // =========================
        // ATTENDANCE
        // =========================

        List<Attendance> attendances =
                attendanceRepository
                        .findByCreatedAtBetween(
                                start,
                                end
                        );

        int workingToday = 0;

        int checkedInNow = 0;

        for (Attendance attendance : attendances) {

            if (
                    attendance.getUser()
                            .getStore()
                            .getId()
                            .equals(storeId)
            ) {

                workingToday++;

                if (
                        attendance.getCheckOutTime()
                                == null
                ) {

                    checkedInNow++;
                }
            }
        }

        int absentToday =
                totalEmployees
                        - workingToday;

        if (absentToday < 0) {

            absentToday = 0;
        }


        // =========================
        // CASH FLOW
        // =========================

        List<CashTransaction> cashList =
                cashRepository
                        .findByCreatedAtBetween(
                                start,
                                end
                        );

        double income = 0;

        double expense = 0;

        for (CashTransaction tx : cashList) {

            if (
                    tx.getStore()
                            .getId()
                            .equals(storeId)
            ) {

                if (
                        tx.getType()
                                ==
                                CashTransactionType.INCOME
                ) {

                    income +=
                            tx.getAmount();
                }

                if (
                        tx.getType()
                                ==
                                CashTransactionType.EXPENSE
                ) {

                    expense +=
                            tx.getAmount();
                }
            }
        }

        return DashboardResponse
                .builder()

                .todayRevenue(
                        revenue
                )

                .todayInvoices(
                        todayInvoices
                )

                .totalCustomers(
                        customers.size()
                )

                .lowStockProducts(
                        lowStock
                )

                .totalDebt(
                        totalDebt
                )

                .todayIncome(
                        income
                )

                .todayExpense(
                        expense
                )
                .totalEmployees(
                        totalEmployees
                )

                .workingToday(
                        workingToday
                )

                .absentToday(
                        absentToday
                )

                .checkedInNow(
                        checkedInNow
                )
                .build();
    }


}