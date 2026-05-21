package com.huaducdat.storemanager.dashboard.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BestSellerResponse {

    private String productName;

    private Integer totalQuantity;

    private Double totalRevenue;
}