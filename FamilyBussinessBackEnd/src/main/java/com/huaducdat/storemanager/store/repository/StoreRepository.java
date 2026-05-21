package com.huaducdat.storemanager.store.repository;

import com.huaducdat.storemanager.store.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository
        extends JpaRepository<Store, Long> {

    List<Store> findByOwnerId(
            Long ownerId
    );

    Optional<Store> findByIdAndOwnerId(
            Long id,
            Long ownerId
    );
}
