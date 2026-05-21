package com.huaducdat.storemanager.cashflow.service;

import com.huaducdat.storemanager.cashflow.model.entity.CashTransaction;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.AuditAction;
import com.huaducdat.storemanager.model.enumtype.CashTransactionType;
import com.huaducdat.storemanager.cashflow.dto.request.CashRequest;
import com.huaducdat.storemanager.cashflow.dto.response.DailyCashReportResponse;
import com.huaducdat.storemanager.cashflow.repository.CashTransactionRepository;
import com.huaducdat.storemanager.audit.service.AuditService;
import com.huaducdat.storemanager.shared.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CashFlowService {

    private final CashTransactionRepository repository;

    private final AuditService auditService;

    public CashFlowService(
            CashTransactionRepository repository, AuditService auditService
    ) {

        this.repository =
                repository;
        this.auditService = auditService;
    }

    // =========================
    // INCOME
    // =========================

    public void income(
            User currentUser,
            CashRequest request
    ) {

        PermissionUtil.requireEmployee(
                currentUser
        );

        CashTransaction tx =
                new CashTransaction();

        tx.setStore(
                currentUser.getStore()
        );

        tx.setCreatedBy(
                currentUser
        );

        tx.setType(
                CashTransactionType.INCOME
        );

        tx.setAmount(
                request.getAmount()
        );

        tx.setNote(
                request.getNote()
        );

        repository.save(tx);

        auditService.log(
                currentUser,
                AuditAction.CASH_INCOME,
                "Cash income: "
                        + request.getAmount()
        );
    }

    // =========================
    // EXPENSE
    // =========================

    public void expense(
            User currentUser,
            CashRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        CashTransaction tx =
                new CashTransaction();

        tx.setStore(
                currentUser.getStore()
        );

        tx.setCreatedBy(
                currentUser
        );

        tx.setType(
                CashTransactionType.EXPENSE
        );

        tx.setAmount(
                request.getAmount()
        );

        tx.setNote(
                request.getNote()
        );

        repository.save(tx);

        auditService.log(
                currentUser,
                AuditAction.CASH_EXPENSE,
                "Cash expense: "
                        + request.getAmount()
        );
    }

    // =========================
    // DAILY REPORT
    // =========================

    public DailyCashReportResponse
    dailyReport(
            User currentUser
    ) {

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

        List<CashTransaction> list =
                repository
                        .findByStoreIdAndCreatedAtBetween(
                                currentUser.getStore()
                                        .getId(),
                                start,
                                end
                        );

        double income = 0;

        double expense = 0;

        for (CashTransaction tx : list) {

            if (tx.getType()
                    == CashTransactionType.INCOME) {

                income += tx.getAmount();
            }

            if (tx.getType()
                    == CashTransactionType.EXPENSE) {

                expense += tx.getAmount();
            }
        }

        return DailyCashReportResponse
                .builder()

                .totalIncome(income)

                .totalExpense(expense)

                .balance(
                        income - expense
                )

                .build();
    }
}
