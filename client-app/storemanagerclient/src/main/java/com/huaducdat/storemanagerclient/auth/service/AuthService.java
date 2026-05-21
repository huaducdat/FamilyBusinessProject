package com.huaducdat.storemanagerclient.auth.service;

import com.huaducdat.storemanagerclient.auth.domain.dto.request.LoginRequest;
import com.huaducdat.storemanagerclient.auth.domain.dto.response.CurrentUserResponse;
import com.huaducdat.storemanagerclient.auth.domain.dto.response.LoginResponse;
import com.huaducdat.storemanagerclient.shared.api.ApiException;
import com.huaducdat.storemanagerclient.shared.session.SessionService;
import com.huaducdat.storemanagerclient.shared.session.SessionState;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AuthService {

    private final AuthApiClient authApiClient;

    private final SessionService sessionService;

    public AuthService(
            AuthApiClient authApiClient,
            SessionService sessionService
    ) {

        this.authApiClient = authApiClient;
        this.sessionService = sessionService;
    }

    public CompletableFuture<SessionState> login(
            String username,
            String password
    ) {

        return CompletableFuture.supplyAsync(
                () -> {

                    LoginResponse response =
                            authApiClient.login(
                                    LoginRequest.builder()
                                            .username(username)
                                            .password(password)
                                            .build()
                            );

                    SessionState sessionState =
                            SessionState.builder()
                                    .token(response.getToken())
                                    .currentUser(response.getUser())
                                    .build();

                    sessionService.setSession(
                            sessionState
                    );

                    return sessionState;
                }
        ).exceptionally(ex -> {

            Throwable cause =
                    unwrap(ex);

            if (cause instanceof ApiException apiException
                    && apiException.isAuthenticationFailure()) {

                throw new CompletionException(apiException);
            }

            throw new CompletionException(cause);
        });
    }

    public CompletableFuture<CurrentUserResponse> loadCurrentUser() {

        return CompletableFuture.supplyAsync(
                () -> {

                    CurrentUserResponse currentUser =
                            authApiClient.me();

                    SessionState currentSession =
                            sessionService.getSessionState();

                    sessionService.setSession(
                            SessionState.builder()
                                    .token(currentSession.getToken())
                                    .currentUser(currentUser)
                                    .build()
                    );

                    return currentUser;
                }
        );
    }

    public CompletableFuture<Void> logout() {

        System.out.println(
                "[AuthService] Logout requested"
        );

        return CompletableFuture.runAsync(
                () -> {

                    try {
                        authApiClient.logout();
                    } finally {
                        sessionService.clearSession();
                        System.out.println(
                                "[AuthService] Session cleared"
                        );
                    }
                }
        );
    }

    private Throwable unwrap(
            Throwable throwable
    ) {

        Throwable current = throwable;

        while (current instanceof CompletionException
                && current.getCause() != null) {
            current = current.getCause();
        }

        return current;
    }
}
