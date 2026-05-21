package com.huaducdat.storemanager.payroll.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayrollResponse {

    private String employeeName;

    private Integer payrollMonth;

    private Integer payrollYear;

    private Double totalHours;

    private Double overtimeHours;

    private Double finalSalary;
}