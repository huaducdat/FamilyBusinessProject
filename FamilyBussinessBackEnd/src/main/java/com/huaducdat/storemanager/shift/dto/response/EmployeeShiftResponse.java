package com.huaducdat.storemanager.shift.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeShiftResponse {

    private String employeeName;

    private String shiftName;

    private String workDate;

    private String startTime;

    private String endTime;
}