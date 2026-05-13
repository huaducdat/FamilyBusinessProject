package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    private Double todayRevenue;

    private Integer todayInvoices;

    private Integer totalCustomers;

    private Integer lowStockProducts;

    private Double totalDebt;

    private Double todayIncome;

    private Double todayExpense;

    private Integer totalEmployees;

    private Integer workingToday;

    private Integer absentToday;

    private Integer checkedInNow;
}