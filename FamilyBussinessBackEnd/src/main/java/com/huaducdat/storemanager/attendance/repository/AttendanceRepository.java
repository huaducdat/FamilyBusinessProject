package com.huaducdat.storemanager.attendance.repository;

import com.huaducdat.storemanager.attendance.model.entity.Attendance;
import com.huaducdat.storemanager.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserAndWorkDate(
            User user,
            LocalDate workDate
    );

    List<Attendance> findByUserId(
            Long userId
    );
    List<Attendance> findByUserIdAndWorkDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );
    List<Attendance> findByUserIdAndUserStoreIdAndWorkDateBetween(
            Long userId,
            Long storeId,
            LocalDate start,
            LocalDate end
    );
    List<Attendance> findByWorkDate(
            LocalDate date
    );

    List<Attendance> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Attendance> findByUserStoreIdAndCreatedAtBetween(
            Long storeId,
            LocalDateTime start,
            LocalDateTime end
    );
}
