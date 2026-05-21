package com.huaducdat.storemanager.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebtPaymentRequest {

    private Long customerId;

    private Double amount;

    private String note;
}