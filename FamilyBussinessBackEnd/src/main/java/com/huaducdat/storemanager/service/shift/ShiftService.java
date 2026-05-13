package com.huaducdat.storemanager.service.shift;

import com.huaducdat.storemanager.model.entity.EmployeeShift;
import com.huaducdat.storemanager.model.entity.Shift;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.request.AssignShiftRequest;
import com.huaducdat.storemanager.model.request.CreateShiftRequest;
import com.huaducdat.storemanager.model.response.EmployeeShiftResponse;
import com.huaducdat.storemanager.model.response.ShiftResponse;
import com.huaducdat.storemanager.repository.EmployeeShiftRepository;
import com.huaducdat.storemanager.repository.ShiftRepository;
import com.huaducdat.storemanager.repository.UserRepository;
import com.huaducdat.storemanager.service.util.PermissionUtil;
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
                        .findById(
                                request.getUserId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        Shift shift =
                shiftRepository
                        .findById(
                                request.getShiftId()
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
            Long userId
    ) {

        return employeeShiftRepository

                .findByUserId(
                        userId
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