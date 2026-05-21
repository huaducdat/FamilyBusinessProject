package com.huaducdat.storemanagerclient;

import com.huaducdat.storemanagerclient.auth.presenter.LoginPresenter;
import com.huaducdat.storemanagerclient.auth.service.AuthApiClient;
import com.huaducdat.storemanagerclient.auth.service.AuthService;
import com.huaducdat.storemanagerclient.auth.view.LoginController;
import com.huaducdat.storemanagerclient.auth.viewmodel.LoginViewModel;
import com.huaducdat.storemanagerclient.dashboard.view.DashboardController;
import com.huaducdat.storemanagerclient.shared.api.ApiClient;
import com.huaducdat.storemanagerclient.shared.config.ConfigService;
import com.huaducdat.storemanagerclient.shared.navigation.NavigationService;
import com.huaducdat.storemanagerclient.shared.navigation.Route;
import com.huaducdat.storemanagerclient.shared.session.SessionService;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AppBootstrap {

    private final ConfigService configService =
            new ConfigService();

    private final SessionService sessionService =
            new SessionService();

    private final ApiClient apiClient =
            new ApiClient(
                    configService,
                    sessionService
            );

    private final AuthApiClient authApiClient =
            new AuthApiClient(
                    apiClient
            );

    private final AuthService authService =
            new AuthService(
                    authApiClient,
                    sessionService
            );

    private final LoginViewModel loginViewModel =
            new LoginViewModel();

    private final NavigationService navigationService =
            new NavigationService();

    private final LoginPresenter loginPresenter =
            new LoginPresenter(
                    loginViewModel,
                    authService,
                    navigationService
            );

    public void start(
            Stage stage
    ) throws Exception {

        navigationService.setControllerFactory(
                createControllerFactory()
        );

        navigationService.start(
                stage,
                Route.LOGIN
        );
    }

    private Callback<Class<?>, Object> createControllerFactory() {

        return controllerType -> {

            System.out.println(
                    "[AppBootstrap] Creating controller: "
                            + controllerType.getName()
            );

            if (controllerType == LoginController.class) {
                try {
                    LoginController controller =
                            new LoginController(
                                    loginViewModel,
                                    loginPresenter
                            );
                    System.out.println(
                            "[AppBootstrap] Created LoginController: "
                                    + controller.getClass().getName()
                    );
                    return controller;
                } catch (Exception ex) {
                    System.out.println(
                            "[AppBootstrap] Failed to create LoginController"
                    );
                    ex.printStackTrace();
                    throw ex;
                }
            }

            if (controllerType == DashboardController.class) {
                try {
                    DashboardController controller =
                            new DashboardController(
                                    authService,
                                    navigationService
                            );
                    System.out.println(
                            "[AppBootstrap] Created DashboardController: "
                                    + controller.getClass().getName()
                    );
                    return controller;
                } catch (Exception ex) {
                    System.out.println(
                            "[AppBootstrap] Failed to create DashboardController"
                    );
                    ex.printStackTrace();
                    throw ex;
                }
            }

            try {
                Object controller = controllerType
                        .getDeclaredConstructor()
                        .newInstance();
                System.out.println(
                        "[AppBootstrap] Created controller via no-arg constructor: "
                                + controller.getClass().getName()
                );
                return controller;
            } catch (Exception ex) {
                System.out.println(
                        "[AppBootstrap] Failed to create controller: "
                                + controllerType.getName()
                );
                ex.printStackTrace();
                throw new RuntimeException(
                        "Unable to create controller: "
                                + controllerType.getName(),
                        ex
                );
            }
        };
    }
}
