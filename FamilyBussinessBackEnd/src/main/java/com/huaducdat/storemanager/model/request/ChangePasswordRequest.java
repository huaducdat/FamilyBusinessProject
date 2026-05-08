package com.huaducdat.storemanager.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    private String oldPassword;

    private String newPassword;
}