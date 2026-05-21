package com.huaducdat.storemanagerclient.auth.presenter;

import com.huaducdat.storemanagerclient.auth.service.AuthService;
import com.huaducdat.storemanagerclient.auth.viewmodel.LoginViewModel;
import com.huaducdat.storemanagerclient.shared.navigation.NavigationService;
import javafx.application.Platform;

import java.util.concurrent.CompletionException;

public class LoginPresenter {

    private final LoginViewModel viewModel;

    private final AuthService authService;

    private final NavigationService navigationService;

    public LoginPresenter(
            LoginViewModel viewModel,
            AuthService authService,
            NavigationService navigationService
    ) {

        this.viewModel = viewModel;
        this.authService = authService;
        this.navigationService = navigationService;
    }

    public void login() {

        viewModel.clearError();
        viewModel.setLoading(true);

        authService.login(
                        viewModel.getUsername(),
                        viewModel.getPassword()
                )
                .thenAccept(session -> runOnUiThread(() -> {

                    viewModel.setLoading(false);
                    viewModel.clearCredentials();

                    try {
                        navigationService.navigateToDashboard();
                    } catch (RuntimeException ex) {
                        viewModel.setErrorMessage(
                                ex.getCause() != null
                                        ? ex.getCause().getMessage()
                                        : ex.getMessage()
                        );
                    }
                }))
                .exceptionally(ex -> {

                    Throwable cause =
                            unwrap(ex);

                    runOnUiThread(() -> {

                        viewModel.setLoading(false);
                        viewModel.setErrorMessage(
                                cause.getMessage() == null
                                        ? "Login failed"
                                        : cause.getMessage()
                        );
                    });

                    return null;
                });
    }

    private void runOnUiThread(
            Runnable runnable
    ) {

        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
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
