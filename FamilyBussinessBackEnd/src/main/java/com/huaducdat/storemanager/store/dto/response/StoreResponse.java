package com.huaducdat.storemanager.store.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreResponse {

    private Long id;

    private Long ownerId;

    private String name;

    private String address;

    private String phone;
}
