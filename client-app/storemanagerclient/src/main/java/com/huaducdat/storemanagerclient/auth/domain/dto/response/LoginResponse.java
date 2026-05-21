package com.huaducdat.storemanagerclient.auth.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {

    private String token;

    private CurrentUserResponse user;
}
