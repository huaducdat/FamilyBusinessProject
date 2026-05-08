package com.huaducdat.storemanager.model.response;

import com.huaducdat.storemanager.model.enumtype.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private Long userId;

    private String username;

    private String fullName;

    private Role role;

    private String token;
}