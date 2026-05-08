package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository
        extends JpaRepository<Store, Long> {
}