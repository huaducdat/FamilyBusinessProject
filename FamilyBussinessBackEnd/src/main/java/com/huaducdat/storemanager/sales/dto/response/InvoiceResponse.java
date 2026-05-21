package com.huaducdat.storemanager.sales.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InvoiceResponse {

    private Long invoiceId;

    private String cashierName;

    private Double totalAmount;

    private Boolean completed;

    private Boolean paid;

    private LocalDateTime createdAt;
}