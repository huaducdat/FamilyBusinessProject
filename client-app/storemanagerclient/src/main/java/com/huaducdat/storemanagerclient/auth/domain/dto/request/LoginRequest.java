package com.huaducdat.storemanagerclient.auth.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {

    private String username;

    private String password;
}
