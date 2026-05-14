package com.huaducdat.controlapp.view.dialog;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TechnicalPasswordDialog {

    private String password;

    public String show() {

        Stage stage =
                new Stage();

        stage.initModality(
                Modality.APPLICATION_MODAL
        );

        stage.setTitle(
                "Technical Authentication"
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
        // UI
        // =========================

        Label title =
                new Label(
                        "Enter Technical Password"
                );

        PasswordField passwordField =
                new PasswordField();

        passwordField.setPromptText(
                "Password"
        );

        Label statusLabel =
                new Label();

        Button confirmButton =
                new Button(
                        "Confirm"
                );

        // =========================
        // ACTION
        // =========================

        confirmButton.setOnAction(e -> {

            String value =
                    passwordField.getText();

            if (value.isBlank()) {

                statusLabel.setText(
                        "Password required"
                );

                return;
            }

            password = value;

            stage.close();
        });

        // =========================
        // ADD
        // =========================

        root.getChildren().addAll(

                title,

                passwordField,

                confirmButton,

                statusLabel
        );

        Scene scene =
                new Scene(
                        root,
                        350,
                        200
                );

        stage.setScene(scene);

        stage.showAndWait();

        return password;
    }
}