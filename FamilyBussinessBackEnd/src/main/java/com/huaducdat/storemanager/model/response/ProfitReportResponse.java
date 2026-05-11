package com.huaducdat.storemanager.model.response;

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