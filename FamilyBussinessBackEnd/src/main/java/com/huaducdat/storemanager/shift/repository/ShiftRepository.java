package com.huaducdat.storemanager.shift.repository;

import com.huaducdat.storemanager.shift.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository
        extends JpaRepository<Shift, Long> {

    List<Shift> findByStoreId(
            Long storeId
    );

    Optional<Shift> findByIdAndStoreId(
            Long id,
            Long storeId
    );
}
