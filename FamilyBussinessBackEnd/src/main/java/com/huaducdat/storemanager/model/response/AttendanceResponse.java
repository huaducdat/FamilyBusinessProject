package com.huaducdat.storemanager.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResponse {

    private Long id;

    private String username;

    private LocalDate workDate;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Double totalHours;

    private Boolean completed;
}