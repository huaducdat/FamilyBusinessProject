package com.huaducdat.controlapp.view.dialog;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetupPasswordDialog {

    private String password;

    public String show() {

        Stage stage =
                new Stage();

        stage.initModality(
                Modality.APPLICATION_MODAL
        );

        stage.setTitle(
                "Setup Technical Password"
        );

        // =========================
        // ROOT
        // =========================

        VBox root =
                new VBox();

        root.setSpacing(15);

        root.setPadding(
                new Insets(20)
        );

        // =========================
        // TITLE
        // =========================

        Label title =
                new Label(
                        "Setup Technical Password"
                );

        // =========================
        // PASSWORD
        // =========================

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Password"
        );

        PasswordField confirmField =
                new PasswordField();

        confirmField.setPromptText(
                "Confirm Password"
        );

        // =========================
        // STATUS
        // =========================

        Label statusLabel =
                new Label();

        // =========================
        // BUTTON
        // =========================

        Button confirmButton =
                new Button(
                        "Setup Password"
                );

        // =========================
        // ACTION
        // =========================

        confirmButton.setOnAction(e -> {

            String pass =
                    passwordField.getText();

            String confirm =
                    confirmField.getText();

            if (pass.isBlank()) {

                statusLabel.setText(
                        "Password required"
                );

                return;
            }

            if (!pass.equals(confirm)) {

                statusLabel.setText(
                        "Password mismatch"
                );

                return;
            }

            password = pass;

            stage.close();
        });

        // =========================
        // ADD
        // =========================

        root.getChildren().addAll(

                title,

                passwordField,

                confirmField,

                confirmButton,

                statusLabel
        );

        Scene scene =
                new Scene(
                        root,
                        400,
                        250
                );

        stage.setScene(scene);

        stage.showAndWait();

        return password;
    }
}