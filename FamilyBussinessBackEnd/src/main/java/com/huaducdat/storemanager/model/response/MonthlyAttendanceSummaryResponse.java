package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyAttendanceSummaryResponse {

    private Integer totalDays;

    private Double totalHours;

    private Double averageHoursPerDay;
}