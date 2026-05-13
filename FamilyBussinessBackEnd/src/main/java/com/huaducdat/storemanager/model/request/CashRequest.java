package com.huaducdat.storemanager.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashRequest {

    private Double amount;

    private String note;
}