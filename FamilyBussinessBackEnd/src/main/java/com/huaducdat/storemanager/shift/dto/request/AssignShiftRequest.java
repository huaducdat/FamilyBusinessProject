package com.huaducdat.storemanager.shift.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignShiftRequest {

    private Long userId;

    private Long shiftId;

    private String workDate;
}