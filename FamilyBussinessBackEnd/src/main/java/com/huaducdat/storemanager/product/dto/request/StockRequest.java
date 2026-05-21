package com.huaducdat.storemanager.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequest {

    private Integer quantity;

    private String note;
}