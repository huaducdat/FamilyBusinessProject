package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyRevenueReportResponse {

    private Integer totalInvoices;

    private Double totalRevenue;

    private Double averageInvoiceValue;
}