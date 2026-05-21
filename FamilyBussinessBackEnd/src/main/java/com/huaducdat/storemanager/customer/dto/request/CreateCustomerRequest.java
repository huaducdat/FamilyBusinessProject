package com.huaducdat.storemanager.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {

    private String fullName;

    private String phone;

    private String address;
}