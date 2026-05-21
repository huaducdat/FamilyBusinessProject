package com.huaducdat.storemanager.dashboard.service;

import com.huaducdat.storemanager.attendance.model.entity.Attendance;
import com.huaducdat.storemanager.cashflow.model.entity.CashTransaction;
import com.huaducdat.storemanager.customer.model.entity.Customer;
import com.huaducdat.storemanager.product.model.entity.Product;
import com.huaducdat.storemanager.sales.model.entity.Invoice;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.CashTransactionType;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import com.huaducdat.storemanager.dashboard.dto.response.DashboardResponse;
import com.huaducdat.storemanager.attendance.repository.AttendanceRepository;
import com.huaducdat.storemanager.cashflow.repository.CashTransactionRepository;
import com.huaducdat.storemanager.customer.repository.CustomerRepository;
import com.huaducdat.storemanager.product.repository.ProductRepository;
import com.huaducdat.storemanager.sales.repository.InvoiceRepository;
import com.huaducdat.storemanager.user.repository.UserRepository;
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
                        .findByStoreIdAndCreatedAtBetween(
                                storeId,
                                start,
                                end
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
                        .findByUserStoreIdAndCreatedAtBetween(
                                storeId,
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
                        .findByStoreIdAndCreatedAtBetween(
                                storeId,
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
