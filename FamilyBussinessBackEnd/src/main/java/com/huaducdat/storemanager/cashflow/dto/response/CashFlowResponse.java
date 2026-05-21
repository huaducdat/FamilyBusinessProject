package com.huaducdat.storemanager.cashflow.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CashFlowResponse {

    private String type;

    private Double amount;

    private String note;
}