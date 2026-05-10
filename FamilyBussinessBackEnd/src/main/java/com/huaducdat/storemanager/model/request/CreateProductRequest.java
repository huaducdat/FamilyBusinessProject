package com.huaducdat.storemanager.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    private Long categoryId;

    private String name;

    private String barcode;

    private String description;

    private Double importPrice;

    private Double sellPrice;

    private Integer stockQuantity;
}