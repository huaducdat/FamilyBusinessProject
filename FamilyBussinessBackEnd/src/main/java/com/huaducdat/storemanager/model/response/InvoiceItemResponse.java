package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceItemResponse {

    private String productName;

    private Double productPrice;

    private Integer quantity;

    private Double totalPrice;
}