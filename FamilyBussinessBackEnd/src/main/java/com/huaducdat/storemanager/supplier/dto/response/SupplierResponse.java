package com.huaducdat.storemanager.supplier.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupplierResponse {

    private Long id;

    private String name;

    private String phone;

    private String address;

    private String email;

    private Boolean active;
}