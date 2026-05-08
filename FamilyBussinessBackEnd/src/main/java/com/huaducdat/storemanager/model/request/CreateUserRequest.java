package com.huaducdat.storemanager.model.request;

import com.huaducdat.storemanager.model.enumtype.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String username;

    private String password;

    private String fullName;

    private String phone;

    private Role role;
}