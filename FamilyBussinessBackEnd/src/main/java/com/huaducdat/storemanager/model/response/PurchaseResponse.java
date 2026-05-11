package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseResponse {

    private Long purchaseOrderId;

    private String supplierName;

    private Double totalAmount;

    private Boolean completed;
}