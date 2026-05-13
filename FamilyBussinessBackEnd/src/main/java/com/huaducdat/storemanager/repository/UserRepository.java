package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.User;
import com.huaducdat.storemanager.model.enumtype.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(
            String username
    );
    List<User> findByStoreId(
            Long storeId
    );

    boolean existsByUserRole(
            UserRole userRole
    );
}