package com.huaducdat.storemanager.supplier.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSupplierRequest {

    private String name;

    private String phone;

    private String address;

    private String email;
}