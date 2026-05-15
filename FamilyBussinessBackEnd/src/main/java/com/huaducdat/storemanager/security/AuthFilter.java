package com.huaducdat.storemanager.security;

import com.huaducdat.storemanager.model.entity.AuthToken;
import com.huaducdat.storemanager.repository.AuthTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthFilter
        extends OncePerRequestFilter {

    private final AuthTokenRepository tokenRepository;

    public AuthFilter(
            AuthTokenRepository tokenRepository
    ) {

        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String path =
                    request.getRequestURI();

            // =========================
            // PUBLIC API
            // =========================

            if (path.startsWith("/api/auth/login")  ||
                    path.equals("/health")) {

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            // =========================
            // TOKEN
            // =========================

            String authHeader =
                    request.getHeader(
                            "Authorization"
                    );

            if (authHeader == null
                    || !authHeader.startsWith(
                    "Bearer "
            )) {

                response.setStatus(401);

                return;
            }

            String token =
                    authHeader.substring(7);

            AuthToken authToken =
                    tokenRepository
                            .findByToken(token)
                            .orElse(null);

            if (authToken == null) {

                response.setStatus(401);

                return;
            }

            // =========================
            // EXPIRE
            // =========================

            if (authToken.getExpiredAt()
                    .isBefore(
                            LocalDateTime.now()
                    )) {

                response.setStatus(401);

                return;
            }

            // =========================
            // SAVE USER
            // =========================

            request.setAttribute(
                    "currentUser",
                    authToken.getUser()
            );

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (Exception ex) {

            response.setStatus(500);
        }
    }
}