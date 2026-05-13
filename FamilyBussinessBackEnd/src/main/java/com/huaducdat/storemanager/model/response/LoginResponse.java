package com.huaducdat.storemanager.model.response;

import com.huaducdat.storemanager.model.enumtype.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

    private Long userId;

    private String username;

    private String fullName;

    private UserRole userRole;

    private String token;
}