package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseResponse<T> {

    private boolean success;

    private String message;

    private T data;
}