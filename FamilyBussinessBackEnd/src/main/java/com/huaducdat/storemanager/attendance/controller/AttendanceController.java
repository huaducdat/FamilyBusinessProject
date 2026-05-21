package com.huaducdat.storemanager.attendance.controller;

import com.huaducdat.storemanager.attendance.model.entity.Attendance;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.attendance.dto.response.AttendanceResponse;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.attendance.service.AttendanceService;
import com.huaducdat.storemanager.shared.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService
    ) {

        this.attendanceService =
                attendanceService;
    }

    // =========================
    // CHECK IN
    // =========================

    @PostMapping("/check-in")
    public BaseResponse<?> checkIn(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Attendance attendance =
                attendanceService.checkIn(
                        currentUser
                );

        return BaseResponse.builder()
                .success(true)
                .message("Check in success")
                .data(
                        toResponse(attendance)
                )
                .build();
    }

    // =========================
    // CHECK OUT
    // =========================

    @PostMapping("/check-out")
    public BaseResponse<?> checkOut(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        Attendance attendance =
                attendanceService.checkOut(
                        currentUser
                );

        return BaseResponse.builder()
                .success(true)
                .message("Check out success")
                .data(
                        toResponse(attendance)
                )
                .build();
    }

    // =========================
    // MAPPER
    // =========================

    private AttendanceResponse toResponse(
            Attendance attendance
    ) {

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .username(
                        attendance.getUser()
                                .getUsername()
                )
                .workDate(
                        attendance.getWorkDate()
                )
                .checkInTime(
                        attendance.getCheckInTime()
                )
                .checkOutTime(
                        attendance.getCheckOutTime()
                )
                .totalHours(
                        attendance.getTotalHours()
                )
                .completed(
                        attendance.getCompleted()
                )
                .build();
    }

    @GetMapping("/my")
    public BaseResponse<?> myAttendances(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        List<AttendanceResponse> list =
                attendanceService
                        .myAttendances(
                                currentUser
                        )
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return BaseResponse.builder()
                .success(true)
                .message("Attendance history")
                .data(list)
                .build();
    }

    @GetMapping("/monthly-summary")
    public BaseResponse<?> monthlySummary(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Monthly summary")
                .data(
                        attendanceService.monthlySummary(
                                currentUser
                        )
                )
                .build();
    }
}
