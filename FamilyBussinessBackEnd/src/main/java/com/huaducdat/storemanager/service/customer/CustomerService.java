package com.huaducdat.storemanager.service.customer;

import com.huaducdat.storemanager.model.entity.*;
import com.huaducdat.storemanager.model.request.CreateCustomerRequest;
import com.huaducdat.storemanager.model.request.DebtPaymentRequest;
import com.huaducdat.storemanager.model.response.CustomerHistoryResponse;
import com.huaducdat.storemanager.model.response.CustomerResponse;
import com.huaducdat.storemanager.model.response.DebtPaymentResponse;
import com.huaducdat.storemanager.model.response.InvoiceResponse;
import com.huaducdat.storemanager.repository.CustomerRepository;
import com.huaducdat.storemanager.repository.DebtPaymentRepository;
import com.huaducdat.storemanager.repository.InvoiceRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final DebtPaymentRepository debtRepository;

    private final InvoiceRepository invoiceRepository;

    public CustomerService(
            CustomerRepository customerRepository, DebtPaymentRepository debtRepository, InvoiceRepository invoiceRepository
    ) {

        this.customerRepository =
                customerRepository;
        this.debtRepository = debtRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // =========================
    // CREATE
    // =========================

    public Customer create(
            User currentUser,
            CreateCustomerRequest request
    ) {

        PermissionUtil.requireEmployee(
                currentUser
        );

        Store store =
                currentUser.getStore();

        Customer customer =
                new Customer();

        customer.setStore(store);

        customer.setFullName(
                request.getFullName()
        );

        customer.setPhone(
                request.getPhone()
        );

        customer.setAddress(
                request.getAddress()
        );

        customer.setDebtAmount(0.0);

        customer.setActive(true);

        return customerRepository.save(customer);
    }

    // =========================
    // LIST
    // =========================

    public List<CustomerResponse> list() {

        return customerRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // RESPONSE
    // =========================

    private CustomerResponse toResponse(
            Customer customer
    ) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(
                        customer.getFullName()
                )
                .phone(
                        customer.getPhone()
                )
                .address(
                        customer.getAddress()
                )
                .debtAmount(
                        customer.getDebtAmount()
                )
                .active(
                        customer.getActive()
                )
                .build();
    }

    public DebtPaymentResponse payDebt(
            User currentUser,
            DebtPaymentRequest request
    ) {

        PermissionUtil.requireEmployee(
                currentUser
        );

        Customer customer =
                customerRepository
                        .findById(
                                request.getCustomerId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Customer not found"
                                )
                        );

        double currentDebt =
                customer.getDebtAmount();

        double remain =
                currentDebt
                        - request.getAmount();

        if (remain < 0) {

            remain = 0;
        }

        // =========================
        // UPDATE DEBT
        // =========================

        customer.setDebtAmount(remain);

        customerRepository.save(customer);

        // =========================
        // PAYMENT HISTORY
        // =========================

        DebtPayment payment =
                new DebtPayment();

        payment.setCustomer(customer);

        payment.setReceivedBy(
                currentUser
        );

        payment.setAmount(
                request.getAmount()
        );

        payment.setNote(
                request.getNote()
        );

        debtRepository.save(payment);

        return DebtPaymentResponse
                .builder()

                .customerName(
                        customer.getFullName()
                )

                .paidAmount(
                        request.getAmount()
                )

                .remainingDebt(
                        remain
                )

                .build();
    }

    public CustomerHistoryResponse history(
            Long customerId
    ) {

        Customer customer =
                customerRepository
                        .findById(customerId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Customer not found"
                                )
                        );

        List<Invoice> invoices =
                invoiceRepository
                        .findByCustomerIdOrderByCreatedAtDesc(
                                customerId
                        );

        double totalSpent =
                invoices.stream()
                        .map(Invoice::getTotalAmount)
                        .reduce(
                                0.0,
                                Double::sum
                        );

        List<InvoiceResponse> invoiceResponses =
                invoices.stream()
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

        return CustomerHistoryResponse
                .builder()

                .customerName(
                        customer.getFullName()
                )

                .debtAmount(
                        customer.getDebtAmount()
                )

                .totalInvoices(
                        invoices.size()
                )

                .totalSpent(
                        totalSpent
                )

                .invoices(
                        invoiceResponses
                )

                .build();
    }
}