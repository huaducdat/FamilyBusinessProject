package com.huaducdat.storemanager.user.dto.request;

import com.huaducdat.storemanager.model.enumtype.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    private String fullName;

    private String phone;

    private UserRole userRole;

    private Boolean active;
}