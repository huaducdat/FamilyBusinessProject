package com.huaducdat.storemanager.report.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyRevenueReportResponse {

    private Integer totalInvoices;

    private Double totalRevenue;

    private Double averageInvoiceValue;
}