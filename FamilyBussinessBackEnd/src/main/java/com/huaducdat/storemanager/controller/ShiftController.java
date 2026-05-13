package com.huaducdat.storemanager.controller;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.AssignShiftRequest;
import com.huaducdat.storemanager.model.request.CreateShiftRequest;
import com.huaducdat.storemanager.model.response.BaseResponse;
import com.huaducdat.storemanager.service.shift.ShiftService;
import com.huaducdat.storemanager.service.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(
            ShiftService shiftService
    ) {

        this.shiftService =
                shiftService;
    }

    // =========================
    // CREATE SHIFT
    // =========================

    @PostMapping
    public BaseResponse<?> createShift(
            HttpServletRequest request,
            @RequestBody CreateShiftRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Shift created")
                .data(
                        shiftService.createShift(
                                currentUser,
                                body
                        )
                )
                .build();
    }

    // =========================
    // LIST SHIFT
    // =========================

    @GetMapping
    public BaseResponse<?> list(
            HttpServletRequest request
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        return BaseResponse.builder()
                .success(true)
                .message("Shift list")
                .data(
                        shiftService.listShifts(
                                currentUser
                        )
                )
                .build();
    }

    // =========================
    // ASSIGN SHIFT
    // =========================

    @PostMapping("/assign")
    public BaseResponse<?> assign(
            HttpServletRequest request,
            @RequestBody AssignShiftRequest body
    ) {

        User currentUser =
                CurrentUserUtil.get(
                        request
                );

        shiftService.assignShift(
                currentUser,
                body
        );

        return BaseResponse.builder()
                .success(true)
                .message("Shift assigned")
                .data(null)
                .build();
    }

    // =========================
    // USER SCHEDULE
    // =========================

    @GetMapping("/user/{userId}")
    public BaseResponse<?> userSchedule(
            @PathVariable Long userId
    ) {

        return BaseResponse.builder()
                .success(true)
                .message("User schedule")
                .data(
                        shiftService.userSchedule(
                                userId
                        )
                )
                .build();
    }
}