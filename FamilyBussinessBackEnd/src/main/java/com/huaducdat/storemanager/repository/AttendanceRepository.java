package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Attendance;
import com.huaducdat.storemanager.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
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
    List<Attendance> findByWorkDate(
            LocalDate date
    );
}