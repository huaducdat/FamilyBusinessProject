package com.huaducdat.storemanager.model.request;

import com.huaducdat.storemanager.model.enumtype.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String fullName;

    private String phone;

    private Role role;

    private Boolean active;
}