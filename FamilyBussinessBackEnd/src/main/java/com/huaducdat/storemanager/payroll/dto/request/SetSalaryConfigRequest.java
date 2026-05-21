package com.huaducdat.storemanager.payroll.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetSalaryConfigRequest {

    private Long userId;

    private Double hourlyRate;

    private Double overtimeRate;

    private Double bonusAmount;

    private Double deductionAmount;
}