package com.huaducdat.storemanager.customer.service;

import com.huaducdat.storemanager.customer.model.entity.Customer;
import com.huaducdat.storemanager.customer.model.entity.DebtPayment;
import com.huaducdat.storemanager.sales.model.entity.Invoice;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.customer.dto.request.CreateCustomerRequest;
import com.huaducdat.storemanager.customer.dto.request.DebtPaymentRequest;
import com.huaducdat.storemanager.customer.dto.response.CustomerHistoryResponse;
import com.huaducdat.storemanager.customer.dto.response.CustomerResponse;
import com.huaducdat.storemanager.customer.dto.response.DebtPaymentResponse;
import com.huaducdat.storemanager.sales.dto.response.InvoiceResponse;
import com.huaducdat.storemanager.customer.repository.CustomerRepository;
import com.huaducdat.storemanager.customer.repository.DebtPaymentRepository;
import com.huaducdat.storemanager.sales.repository.InvoiceRepository;
import com.huaducdat.storemanager.audit.service.AuditService;
import com.huaducdat.storemanager.shared.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final DebtPaymentRepository debtRepository;

    private final InvoiceRepository invoiceRepository;

    private final AuditService auditService;

    public CustomerService(
            CustomerRepository customerRepository, DebtPaymentRepository debtRepository, InvoiceRepository invoiceRepository, AuditService auditService
    ) {

        this.customerRepository =
                customerRepository;
        this.debtRepository = debtRepository;
        this.invoiceRepository = invoiceRepository;
        this.auditService = auditService;
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

    public List<CustomerResponse> list(
            User currentUser
    ) {

        return customerRepository.findByStoreId(
                        currentUser
                                .getStore()
                                .getId()
                )
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
                        .findByIdAndStoreId(
                                request.getCustomerId()
                                ,
                                currentUser.getStore()
                                        .getId()
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

        auditService.log(
                currentUser,
                AuditAction.PAY_DEBT,
                "Customer debt payment: "
                        + customer.getFullName()
        );

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
            User currentUser,
            Long customerId
    ) {

        Customer customer =
                customerRepository
                        .findByIdAndStoreId(
                                customerId,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Customer not found"
                                )
                        );

        // TODO: future full tenant isolation; this is a lightweight store guard for history reads.
        List<Invoice> invoices =
                invoiceRepository
                        .findByCustomerIdAndStoreIdOrderByCreatedAtDesc(
                                customerId,
                                currentUser.getStore()
                                        .getId()
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
