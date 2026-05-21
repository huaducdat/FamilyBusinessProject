package com.huaducdat.storemanager.attendance.model.entity;

import com.huaducdat.storemanager.shared.model.entity.BaseEntity;
import com.huaducdat.storemanager.user.model.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Getter
@Setter
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // USER
    // =========================

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // =========================
    // DATE
    // =========================

    private LocalDate workDate;

    // =========================
    // TIME
    // =========================

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    // =========================
    // RESULT
    // =========================

    private Double totalHours;

    // =========================
    // STATUS
    // =========================

    private Boolean completed = false;

    // =========================
    // CALCULATE
    // =========================

    public void calculateHours() {

        if (checkInTime == null
                || checkOutTime == null) {

            totalHours = 0.0;

            return;
        }

        long minutes =
                Duration.between(
                        checkInTime,
                        checkOutTime
                ).toMinutes();

        totalHours =
                minutes / 60.0;
    }
}
