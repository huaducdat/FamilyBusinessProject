package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository
        extends JpaRepository<Shift, Long> {

    List<Shift> findByStoreId(
            Long storeId
    );
}