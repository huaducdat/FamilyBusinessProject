package com.huaducdat.storemanager.shift.service;

import com.huaducdat.storemanager.shift.model.entity.EmployeeShift;
import com.huaducdat.storemanager.shift.model.entity.Shift;
import com.huaducdat.storemanager.user.model.entity.User;
import com.huaducdat.storemanager.shift.dto.request.AssignShiftRequest;
import com.huaducdat.storemanager.shift.dto.request.CreateShiftRequest;
import com.huaducdat.storemanager.shift.dto.response.EmployeeShiftResponse;
import com.huaducdat.storemanager.shift.dto.response.ShiftResponse;
import com.huaducdat.storemanager.shift.repository.EmployeeShiftRepository;
import com.huaducdat.storemanager.shift.repository.ShiftRepository;
import com.huaducdat.storemanager.user.repository.UserRepository;
import com.huaducdat.storemanager.shared.util.PermissionUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;

    private final EmployeeShiftRepository employeeShiftRepository;

    private final UserRepository userRepository;

    public ShiftService(
            ShiftRepository shiftRepository,
            EmployeeShiftRepository employeeShiftRepository,
            UserRepository userRepository
    ) {

        this.shiftRepository =
                shiftRepository;

        this.employeeShiftRepository =
                employeeShiftRepository;

        this.userRepository =
                userRepository;
    }

    // =========================
    // CREATE SHIFT
    // =========================

    public ShiftResponse createShift(
            User currentUser,
            CreateShiftRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        Shift shift =
                new Shift();

        shift.setStore(
                currentUser.getStore()
        );

        shift.setName(
                request.getName()
        );

        shift.setStartTime(
                LocalTime.parse(
                        request.getStartTime()
                )
        );

        shift.setEndTime(
                LocalTime.parse(
                        request.getEndTime()
                )
        );

        Shift savedShift =
                shiftRepository.save(
                        shift
                );

        return toResponse(
                savedShift
        );
    }

    // =========================
    // LIST SHIFT
    // =========================

    public List<ShiftResponse> listShifts(
            User currentUser
    ) {

        return shiftRepository

                .findByStoreId(
                        currentUser
                                .getStore()
                                .getId()
                )

                .stream()

                .map(shift ->

                        ShiftResponse
                                .builder()

                                .id(
                                        shift.getId()
                                )

                                .name(
                                        shift.getName()
                                )

                                .startTime(
                                        shift.getStartTime()
                                                .toString()
                                )

                                .endTime(
                                        shift.getEndTime()
                                                .toString()
                                )

                                .build()
                )

                .toList();
    }

    // =========================
    // ASSIGN SHIFT
    // =========================

    public void assignShift(
            User currentUser,
            AssignShiftRequest request
    ) {

        PermissionUtil.requireManager(
                currentUser
        );

        User user =
                userRepository
                        .findByIdAndStoreId(
                                request.getUserId()
                                ,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Shift shift =
                shiftRepository
                        .findByIdAndStoreId(
                                request.getShiftId()
                                ,
                                currentUser.getStore()
                                        .getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Shift not found"
                                )
                        );

        EmployeeShift employeeShift =
                new EmployeeShift();

        employeeShift.setUser(user);

        employeeShift.setShift(shift);

        employeeShift.setWorkDate(
                LocalDate.parse(
                        request.getWorkDate()
                )
        );

        employeeShiftRepository.save(
                employeeShift
        );
    }

    // =========================
    // USER SCHEDULE
    // =========================

    public List<EmployeeShiftResponse>
    userSchedule(
            User currentUser,
            Long userId
    ) {

        // TODO: future full tenant isolation; this is a lightweight store guard for schedule reads.
        userRepository
                .findByIdAndStoreId(
                        userId,
                        currentUser.getStore()
                                .getId()
                )
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"
                        )
                );

        return employeeShiftRepository

                .findByUserIdAndUserStoreId(
                        userId,
                        currentUser.getStore()
                                .getId()
                )

                .stream()

                .map(item ->

                        EmployeeShiftResponse
                                .builder()

                                .employeeName(
                                        item.getUser()
                                                .getFullName()
                                )

                                .shiftName(
                                        item.getShift()
                                                .getName()
                                )

                                .workDate(
                                        item.getWorkDate()
                                                .toString()
                                )

                                .startTime(
                                        item.getShift()
                                                .getStartTime()
                                                .toString()
                                )

                                .endTime(
                                        item.getShift()
                                                .getEndTime()
                                                .toString()
                                )

                                .build()
                )

                .toList();
    }

    private ShiftResponse toResponse(
            Shift shift
    ) {

        return ShiftResponse.builder()

                .id(
                        shift.getId()
                )

                .name(
                        shift.getName()
                )

                .startTime(
                        shift.getStartTime()
                                .toString()
                )

                .endTime(
                        shift.getEndTime()
                                .toString()
                )

                .build();
    }
}
