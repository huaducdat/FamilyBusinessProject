package com.huaducdat.storemanagerclient.controller;

import com.huaducdat.storemanagerclient.presenter.LoginPresenter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private final LoginPresenter presenter =
            new LoginPresenter(this);

    // =========================
    // LOGIN
    // =========================

    @FXML
    public void onLogin() {

        presenter.login(
                usernameField.getText(),
                passwordField.getText()
        );
    }

    // =========================
    // UI
    // =========================

    public void showError(
            String message
    ) {

        statusLabel.setStyle(
                "-fx-text-fill: red;"
        );

        statusLabel.setText(message);
    }

    public void showSuccess(
            String message
    ) {

        statusLabel.setStyle(
                "-fx-text-fill: #00ff99;"
        );

        statusLabel.setText(message);
    }

    public void openDashboard()
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(

                        getClass()
                                .getResource(
                                        "/view/dashboard/DashboardView.fxml"
                                )
                );

        Scene scene =
                new Scene(
                        loader.load()
                );

        Stage stage =
                (Stage) usernameField
                        .getScene()
                        .getWindow();

        stage.setScene(scene);
    }
}