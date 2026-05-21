package com.huaducdat.storemanager.customer.dto.response;

import com.huaducdat.storemanager.sales.dto.response.InvoiceResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CustomerHistoryResponse {

    private String customerName;

    private Double debtAmount;

    private Integer totalInvoices;

    private Double totalSpent;

    private List<InvoiceResponse> invoices;

}