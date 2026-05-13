package com.huaducdat.storemanager.service.cash;

import com.huaducdat.storemanager.model.entity.CashTransaction;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.CashTransactionType;
import com.huaducdat.storemanager.model.request.CashRequest;
import com.huaducdat.storemanager.model.response.DailyCashReportResponse;
import com.huaducdat.storemanager.repository.CashTransactionRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CashFlowService {

    private final CashTransactionRepository repository;

    public CashFlowService(
            CashTransactionRepository repository
    ) {

        this.repository =
                repository;
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
    }

    // =========================
    // DAILY REPORT
    // =========================

    public DailyCashReportResponse
    dailyReport() {

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
                        .findByCreatedAtBetween(
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