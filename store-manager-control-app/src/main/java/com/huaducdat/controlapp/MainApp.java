package com.huaducdat.controlapp;

import com.huaducdat.controlapp.presenter.MainPresenter;
import com.huaducdat.controlapp.service.BackendProcessService;
import com.huaducdat.controlapp.service.DatabaseBackupService;
import com.huaducdat.controlapp.service.DatabaseRestoreService;
import com.huaducdat.controlapp.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.huaducdat.controlapp.service.PasswordManagerService;
import com.huaducdat.controlapp.service.TechnicalAuthService;
import com.huaducdat.controlapp.view.dialog.SetupPasswordDialog;

public class MainApp
        extends Application {

    @Override
    public void start(
            Stage stage
    ) {

        // =========================
        // SERVICES
        // =========================

        TechnicalAuthService
                technicalAuthService =

                new TechnicalAuthService();

        PasswordManagerService
                passwordManagerService =

                new PasswordManagerService();

        if (!technicalAuthService.hasPassword()) {

            SetupPasswordDialog dialog =
                    new SetupPasswordDialog();

            String password =
                    dialog.show();

            if (
                    password == null
                            ||
                            password.isBlank()
            ) {

                System.exit(0);
            }

            passwordManagerService.setupPassword(
                    password
            );
        }

        BackendProcessService
                backendProcessService =

                new BackendProcessService();

        DatabaseBackupService
                databaseBackupService =

                new DatabaseBackupService();

        DatabaseRestoreService
                databaseRestoreService =

                new DatabaseRestoreService();

        // =========================
        // VIEW
        // =========================

        MainView view =
                new MainView();

        // =========================
        // PRESENTER
        // =========================

        MainPresenter presenter =
                new MainPresenter(
                        view,
                        backendProcessService,
                        databaseBackupService,
                        databaseRestoreService,
                        technicalAuthService,
                        passwordManagerService
                );

        view.setPresenter(
                presenter
        );

        // =========================
        // SCENE
        // =========================

        Scene scene =
                new Scene(
                        view.getRoot(),
                        700,
                        500
                );

        stage.setTitle(
                "Store Manager Control App"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(
            String[] args
    ) {

        launch(args);
    }
}