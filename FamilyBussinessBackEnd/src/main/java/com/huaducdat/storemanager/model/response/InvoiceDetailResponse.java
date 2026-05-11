package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class InvoiceDetailResponse {

    private Long invoiceId;

    private String cashierName;

    private Double totalAmount;

    private Boolean completed;

    private Boolean paid;

    private LocalDateTime createdAt;

    private List<InvoiceItemResponse> items;
}