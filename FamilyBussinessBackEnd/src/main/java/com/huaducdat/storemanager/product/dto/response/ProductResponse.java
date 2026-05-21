package com.huaducdat.storemanager.product.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {

    private Long id;

    private String categoryName;

    private String name;

    private String barcode;

    private String description;

    private Double importPrice;

    private Double sellPrice;

    private Integer stockQuantity;

    private Boolean active;
}