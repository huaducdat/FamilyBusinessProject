package com.huaducdat.storemanager.auth.dto.response;

import com.huaducdat.storemanager.model.enumtype.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CurrentUserResponse {

    private Long id;

    private Long userId;

    private String username;

    private String fullName;

    private UserRole userRole;

    private Long ownerId;

    private Long storeId;

    private String storeName;

    private Boolean active;
}
