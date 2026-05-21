package com.huaducdat.storemanager.report.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfitReportResponse {

    private Double totalRevenue;

    private Double estimatedCost;

    private Double estimatedProfit;

    private Double marginPercent;
}