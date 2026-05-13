package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.EmployeeShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeShiftRepository
        extends JpaRepository<EmployeeShift, Long> {

    List<EmployeeShift>
    findByUserId(
            Long userId
    );

    List<EmployeeShift>
    findByWorkDate(
            LocalDate date
    );
}