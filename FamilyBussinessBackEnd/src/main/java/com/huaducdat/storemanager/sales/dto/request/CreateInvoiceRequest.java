package com.huaducdat.storemanager.sales.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInvoiceRequest {

    // =========================
    // CUSTOMER
    // nullable = anonymous
    // =========================

    private Long customerId;

    // =========================
    // PRODUCT
    // =========================

    private Long productId;

    // =========================
    // QUANTITY
    // =========================

    private Integer quantity;

    // =========================
    // DEBT
    // =========================

    private Boolean payLater = false;
}