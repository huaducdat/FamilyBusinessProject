package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShiftResponse {

    private Long id;

    private String name;

    private String startTime;

    private String endTime;
}