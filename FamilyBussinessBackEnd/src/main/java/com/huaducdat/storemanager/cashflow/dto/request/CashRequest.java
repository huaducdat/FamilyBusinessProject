package com.huaducdat.storemanager.cashflow.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashRequest {

    private Double amount;

    private String note;
}