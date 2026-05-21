package com.huaducdat.storemanager.store.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStoreRequest {

    private String name;

    private String address;

    private String phone;
}