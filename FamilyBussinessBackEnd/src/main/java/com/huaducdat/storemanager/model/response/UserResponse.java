package com.huaducdat.storemanager.model.response;

import com.huaducdat.storemanager.model.enumtype.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String phone;

    private Role role;

    private Boolean active;
}