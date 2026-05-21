package com.huaducdat.storemanager.shift.dto.response;

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