package com.huaducdat.storemanager.service.attendance;

import com.huaducdat.storemanager.model.entity.Attendance;
import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.response.MonthlyAttendanceSummaryResponse;
import com.huaducdat.storemanager.repository.AttendanceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository
    ) {

        this.attendanceRepository =
                attendanceRepository;
    }

    // =========================
    // CHECK IN
    // =========================

    public Attendance checkIn(
            User currentUser
    ) {

        LocalDate today =
                LocalDate.now();

        boolean existed =
                attendanceRepository
                        .findByUserAndWorkDate(
                                currentUser,
                                today
                        )
                        .isPresent();

        if (existed) {

            throw new RuntimeException(
                    "Already checked in"
            );
        }

        Attendance attendance =
                new Attendance();

        attendance.setUser(currentUser);

        attendance.setWorkDate(today);

        attendance.setCheckInTime(
                LocalDateTime.now()
        );

        attendance.setCompleted(false);

        return attendanceRepository
                .save(attendance);
    }

    // =========================
    // CHECK OUT
    // =========================

    public Attendance checkOut(
            User currentUser
    ) {

        Attendance attendance =
                attendanceRepository
                        .findByUserAndWorkDate(
                                currentUser,
                                LocalDate.now()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Check in first"
                                )
                        );

        if (attendance.getCompleted()) {

            throw new RuntimeException(
                    "Already checked out"
            );
        }

        attendance.setCheckOutTime(
                LocalDateTime.now()
        );

        attendance.setCompleted(true);

        attendance.calculateHours();

        return attendanceRepository
                .save(attendance);
    }

    public List<Attendance> myAttendances(
            User currentUser
    ) {

        return attendanceRepository
                .findByUserId(
                        currentUser.getId()
                );
    }

    public MonthlyAttendanceSummaryResponse monthlySummary(
            User currentUser
    ) {

        LocalDate now =
                LocalDate.now();

        LocalDate start =
                now.withDayOfMonth(1);

        LocalDate end =
                now.withDayOfMonth(
                        now.lengthOfMonth()
                );

        List<Attendance> list =
                attendanceRepository
                        .findByUserIdAndWorkDateBetween(
                                currentUser.getId(),
                                start,
                                end
                        );

        int totalDays =
                (int) list.stream()
                        .filter(Attendance::getCompleted)
                        .count();

        double totalHours =
                list.stream()
                        .map(Attendance::getTotalHours)
                        .filter(hours -> hours != null)
                        .reduce(
                                0.0,
                                Double::sum
                        );

        double average =
                totalDays == 0
                        ? 0
                        : totalHours / totalDays;

        return MonthlyAttendanceSummaryResponse
                .builder()
                .totalDays(totalDays)
                .totalHours(totalHours)
                .averageHoursPerDay(average)
                .build();
    }
}