package com.huaducdat.storemanager.model.request;

import com.huaducdat.storemanager.model.enumtype.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String username;

    private String password;

    private String fullName;

    private String phone;

    private UserRole userRole;
}