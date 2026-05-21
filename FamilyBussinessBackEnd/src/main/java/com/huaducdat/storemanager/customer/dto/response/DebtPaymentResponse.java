package com.huaducdat.storemanager.customer.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DebtPaymentResponse {

    private String customerName;

    private Double paidAmount;

    private Double remainingDebt;
}