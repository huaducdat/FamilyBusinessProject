package com.huaducdat.storemanager.service.auth;

import com.huaducdat.storemanager.repository.AuthTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenCleanupService {

    private final AuthTokenRepository tokenRepository;

    public TokenCleanupService(
            AuthTokenRepository tokenRepository
    ) {

        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanup() {

        tokenRepository.deleteByExpiredAtBefore(
                LocalDateTime.now()
        );

        System.out.println(
                "🔥 TOKEN CLEANUP DONE"
        );
    }
}