package com.huaducdat.storemanager.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOwnerRequest {

    private String username;

    private String password;

    private String fullName;

    private String phone;
}