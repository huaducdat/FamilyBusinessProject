package com.huaducdat.storemanager.sales.dto.response;

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