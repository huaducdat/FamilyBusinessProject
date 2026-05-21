package com.huaducdat.storemanager.customer.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String address;

    private Double debtAmount;

    private Boolean active;
}