package com.huaducdat.storemanagerclient.shared.session;

import com.huaducdat.storemanagerclient.auth.domain.dto.response.CurrentUserResponse;

public class SessionService {

    private final Object lock = new Object();

    private SessionState sessionState =
            SessionState.builder().build();

    public SessionState getSessionState() {

        synchronized (lock) {
            return sessionState;
        }
    }

    public String getToken() {

        synchronized (lock) {
            return sessionState.getToken();
        }
    }

    public CurrentUserResponse getCurrentUser() {

        synchronized (lock) {
            return sessionState.getCurrentUser();
        }
    }

    public boolean isLoggedIn() {

        synchronized (lock) {
            return sessionState.hasToken();
        }
    }

    public void setSession(
            SessionState newSession
    ) {

        synchronized (lock) {
            this.sessionState = newSession == null
                    ? SessionState.builder().build()
                    : newSession;
        }
    }

    public void setSession(
            String token,
            CurrentUserResponse currentUser
    ) {

        setSession(
                SessionState.builder()
                        .token(token)
                        .currentUser(currentUser)
                        .build()
        );
    }

    public void clearSession() {

        synchronized (lock) {
            sessionState = SessionState.builder().build();
        }
    }
}
