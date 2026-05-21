package com.huaducdat.storemanager.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {

    private String name;

    private String description;
}