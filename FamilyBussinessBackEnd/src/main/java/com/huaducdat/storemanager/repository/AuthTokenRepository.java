package com.huaducdat.storemanager.repository;

import com.huaducdat.storemanager.model.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthTokenRepository
        extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByToken(
            String token
    );

    void deleteByToken(
            String token
    );

    void deleteByExpiredAtBefore(
            LocalDateTime time
    );
}