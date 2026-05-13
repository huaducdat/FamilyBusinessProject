package com.huaducdat.storemanager.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShiftRequest {

    private String name;

    private String startTime;

    private String endTime;
}