package com.huaducdat.controlapp.presenter;

import com.huaducdat.controlapp.service.*;
import com.huaducdat.controlapp.view.MainView;
import com.huaducdat.controlapp.view.dialog.ChangePasswordDialog;
import com.huaducdat.controlapp.view.dialog.TechnicalPasswordDialog;
import javafx.stage.FileChooser;

import java.io.File;

public class MainPresenter {

    private final MainView view;

    private final BackendProcessService
            backendProcessService;

    private final DatabaseBackupService
            databaseBackupService;

    private final DatabaseRestoreService
            databaseRestoreService;

    private final TechnicalAuthService technicalAuthService;

    private final PasswordManagerService
            passwordManagerService;

    public MainPresenter(
            MainView view,
            BackendProcessService backendProcessService, DatabaseBackupService databaseBackupService, DatabaseRestoreService databaseRestoreService, TechnicalAuthService technicalAuthService, PasswordManagerService passwordManagerService
    ) {

        this.view =
                view;

        this.backendProcessService =
                backendProcessService;
        this.databaseBackupService = databaseBackupService;
        this.databaseRestoreService = databaseRestoreService;
        this.technicalAuthService = technicalAuthService;
        this.passwordManagerService = passwordManagerService;
    }

    // =========================
    // START
    // =========================

    public void onStartBackend() {

        try {

            backendProcessService.start();

            view.updateStatus(
                    "Backend Status: RUNNING"
            );

        } catch (Exception e) {

            e.printStackTrace();

            view.updateStatus(
                    "Start failed"
            );
        }
    }

    // =========================
    // STOP
    // =========================

    public void onStopBackend() {

        try {

            backendProcessService.stop();

            view.updateStatus(
                    "Backend Status: STOPPED"
            );

        } catch (Exception e) {

            e.printStackTrace();

            view.updateStatus(
                    "Stop failed"
            );
        }
    }

    // =========================
    // BACKUP
    // =========================

    public void onBackupDatabase() {
        if (!requireTechnicalAuth()) {

            return;
        }
        try {

            databaseBackupService.backup();

            view.updateStatus(
                    "Database backup success"
            );

        } catch (Exception e) {

            e.printStackTrace();

            view.updateStatus(
                    "Database backup failed"
            );
        }
    }

    // =========================
    // RESTORE
    // =========================

    public void onRestoreDatabase() {
        if (!requireTechnicalAuth()) {

            return;
        }
        try {

            FileChooser chooser =
                    new FileChooser();

            chooser.setTitle(
                    "Choose SQL Backup File"
            );

            chooser.getExtensionFilters().add(

                    new FileChooser.ExtensionFilter(
                            "SQL Files",
                            "*.sql"
                    )
            );

            File file =
                    chooser.showOpenDialog(
                            null
                    );

            if (file == null) {

                return;
            }

            databaseRestoreService.restore(
                    file
            );

            view.updateStatus(
                    "Database restore success"
            );

        } catch (Exception e) {

            e.printStackTrace();

            view.updateStatus(
                    "Database restore failed"
            );
        }
    }

    // =========================
    // REQUIRE TECHNICAL AUTHEN
    // =========================

    private boolean requireTechnicalAuth() {

        TechnicalPasswordDialog dialog =
                new TechnicalPasswordDialog();

        String password =
                dialog.show();

        if (
                password == null
                        ||
                        password.isBlank()
        ) {

            return false;
        }

        boolean valid =

                technicalAuthService.validate(
                        password
                );

        if (!valid) {

            view.updateStatus(
                    "Invalid technical password"
            );
        }

        return valid;
    }

    // =========================
    // CHANGE PASSWORD
    // =========================

    public void onChangePassword() {

        try {

            ChangePasswordDialog dialog =
                    new ChangePasswordDialog();

            ChangePasswordDialog.Result result =
                    dialog.show();

            if (result == null) {

                return;
            }

            boolean success =

                    passwordManagerService.changePassword(

                            result.oldPassword(),

                            result.newPassword()
                    );

            if (success) {

                view.updateStatus(
                        "Password changed"
                );

            } else {

                view.updateStatus(
                        "Old password incorrect"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            view.updateStatus(
                    "Password change failed"
            );
        }
    }
}