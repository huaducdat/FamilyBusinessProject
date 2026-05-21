package com.huaducdat.storemanager.cashflow.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyCashReportResponse {

    private Double totalIncome;

    private Double totalExpense;

    private Double balance;
}