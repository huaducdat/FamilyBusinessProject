package com.huaducdat.storemanager.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePurchaseRequest {

    private Long supplierId;

    private Long productId;

    private Integer quantity;
}