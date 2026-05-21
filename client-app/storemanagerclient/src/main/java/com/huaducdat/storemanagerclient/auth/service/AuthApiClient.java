package com.huaducdat.storemanagerclient.auth.service;

import com.huaducdat.storemanagerclient.auth.domain.dto.request.LoginRequest;
import com.huaducdat.storemanagerclient.auth.domain.dto.response.CurrentUserResponse;
import com.huaducdat.storemanagerclient.auth.domain.dto.response.LoginResponse;
import com.huaducdat.storemanagerclient.shared.api.ApiException;
import com.huaducdat.storemanagerclient.shared.api.ApiClient;
import com.huaducdat.storemanagerclient.shared.api.ApiResponse;

public class AuthApiClient {

    private final ApiClient apiClient;

    public AuthApiClient(
            ApiClient apiClient
    ) {

        this.apiClient = apiClient;
    }

    public LoginResponse login(
            LoginRequest request
    ) {

        ApiResponse<LoginResponse> response =
                apiClient.post(
                        "/api/auth/login",
                        request,
                        LoginResponse.class
                );

        LoginResponse loginResponse =
                response.getData();

        if (loginResponse == null) {
            throw new ApiException(
                    response.getStatusCode(),
                    "Login response was empty",
                    null,
                    false
            );
        }

        return loginResponse;
    }

    public CurrentUserResponse me() {

        ApiResponse<CurrentUserResponse> response =
                apiClient.get(
                        "/api/auth/me",
                        CurrentUserResponse.class
                );

        CurrentUserResponse currentUser =
                response.getData();

        if (currentUser == null) {
            throw new ApiException(
                    response.getStatusCode(),
                    "Current user response was empty",
                    null,
                    false
            );
        }

        return currentUser;
    }

    public void logout() {

        apiClient.post(
                "/api/auth/logout",
                null,
                Void.class
        );
    }
}
