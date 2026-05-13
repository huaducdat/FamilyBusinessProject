package com.huaducdat.storemanager.model.response;

import com.huaducdat.storemanager.model.enumtype.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String fullName;

    private String phone;

    private UserRole userRole;

    private Boolean active;

    private Long storeId;

    private String storeName;
}