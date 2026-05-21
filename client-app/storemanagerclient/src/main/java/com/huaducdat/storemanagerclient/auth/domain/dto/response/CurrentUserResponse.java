package com.huaducdat.storemanagerclient.auth.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentUserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String email;

    private String role;
}
