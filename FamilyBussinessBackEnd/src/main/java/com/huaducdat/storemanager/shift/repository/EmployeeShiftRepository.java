package com.huaducdat.storemanager.shift.repository;

import com.huaducdat.storemanager.shift.model.entity.EmployeeShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeShiftRepository
        extends JpaRepository<EmployeeShift, Long> {

    List<EmployeeShift>
    findByUserId(
            Long userId
    );

    List<EmployeeShift> findByUserIdAndUserStoreId(
            Long userId,
            Long storeId
    );

    List<EmployeeShift>
    findByWorkDate(
            LocalDate date
    );
}
