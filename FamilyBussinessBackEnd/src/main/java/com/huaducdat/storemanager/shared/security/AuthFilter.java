package com.huaducdat.storemanager.shared.security;

import com.huaducdat.storemanager.auth.model.entity.AuthToken;
import com.huaducdat.storemanager.store.model.entity.Store;
import com.huaducdat.storemanager.shared.response.BaseResponse;
import com.huaducdat.storemanager.auth.repository.AuthTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaducdat.storemanager.shared.exception.UnauthorizedException;
import com.huaducdat.storemanager.shared.security.BearerTokenUtil;
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

    private final ObjectMapper objectMapper;

    public AuthFilter(
            AuthTokenRepository tokenRepository,
            ObjectMapper objectMapper
    ) {

        this.tokenRepository = tokenRepository;
        this.objectMapper = objectMapper;
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

            String token =
                    BearerTokenUtil.extractBearerToken(
                            authHeader
                    );

            AuthToken authToken =
                    tokenRepository
                            .findByToken(token)
                            .orElse(null);

            if (authToken == null) {

                writeUnauthorized(
                        response,
                        "Invalid token"
                );

                return;
            }

            // =========================
            // EXPIRE
            // =========================

            if (authToken.getExpiredAt() == null
                    || authToken.getExpiredAt()
                    .isBefore(
                            LocalDateTime.now()
                    )) {

                writeUnauthorized(
                        response,
                        "Expired token"
                );

                return;
            }

            if (authToken.getUser() == null) {

                writeUnauthorized(
                        response,
                        "Invalid token"
                );

                return;
            }

            Store currentStore =
                    authToken.getStore();

            if (currentStore == null) {
                currentStore =
                        authToken.getUser()
                                .getStore();
            }

            if (currentStore != null) {
                authToken.getUser()
                        .setStore(
                                currentStore
                        );
            }

            // =========================
            // SAVE USER
            // =========================

            request.setAttribute(
                    "currentUser",
                    authToken.getUser()
            );

            request.setAttribute(
                    "currentStore",
                    currentStore
            );

            request.setAttribute(
                    "currentOwnerId",
                    currentStore != null && currentStore.getOwner() != null
                            ? currentStore.getOwner()
                            .getId()
                            : null
            );

            filterChain.doFilter(
                    request,
                    response
            );

        } catch (UnauthorizedException ex) {

            writeUnauthorized(
                    response,
                    ex.getMessage()
            );

        } catch (Exception ex) {

            writeError(
                    response,
                    "Authentication error"
            );
        }
    }

    private void writeUnauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        writeResponse(
                response,
                401,
                message
        );
    }

    private void writeError(
            HttpServletResponse response,
            String message
    ) throws IOException {

        writeResponse(
                response,
                500,
                message
        );
    }

    private void writeResponse(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        BaseResponse.fail(message)
                )
        );
    }
}
