package com.huaducdat.storemanager.auth.repository;

import com.huaducdat.storemanager.auth.model.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository
        extends JpaRepository<Owner, Long> {

    Optional<Owner> findByUsername(
            String username
    );

    boolean existsByUsername(
            String username
    );
}
