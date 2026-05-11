package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardResponse {

    private Double todayRevenue;

    private Integer todayInvoiceCount;

    private Integer lowStockCount;

    private Integer attendanceTodayCount;
}