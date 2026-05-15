package com.huaducdat.storemanagerclient.presenter;

import com.huaducdat.storemanagerclient.api.AuthApiService;
import com.huaducdat.storemanagerclient.controller.LoginController;
import com.huaducdat.storemanagerclient.service.TokenStorageService;

public class LoginPresenter {

    private final LoginController view;

    private final AuthApiService authApi =
            new AuthApiService();

    public LoginPresenter(
            LoginController view
    ) {

        this.view = view;
    }

    // =========================
    // LOGIN
    // =========================

    public void login(
            String username,
            String password
    ) {

        try {

            String token =
                    authApi.login(
                            username,
                            password
                    );

            // =========================
            // SAVE TOKEN
            // =========================

            TokenStorageService.setToken(
                    token
            );

            view.showSuccess(
                    "Login success"
            );

            // =========================
            // OPEN DASHBOARD
            // =========================

            view.openDashboard();

        } catch (Exception ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }
}