package com.huaducdat.controlapp.view.dialog;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ChangePasswordDialog {

    private String oldPassword;

    private String newPassword;

    public Result show() {

        Stage stage =
                new Stage();

        stage.initModality(
                Modality.APPLICATION_MODAL
        );

        stage.setTitle(
                "Change Technical Password"
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
        // FIELDS
        // =========================

        PasswordField oldField =
                new PasswordField();

        oldField.setPromptText(
                "Old Password"
        );

        PasswordField newField =
                new PasswordField();

        newField.setPromptText(
                "New Password"
        );

        PasswordField confirmField =
                new PasswordField();

        confirmField.setPromptText(
                "Confirm New Password"
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
                        "Change Password"
                );

        // =========================
        // ACTION
        // =========================

        confirmButton.setOnAction(e -> {

            String oldPass =
                    oldField.getText();

            String newPass =
                    newField.getText();

            String confirmPass =
                    confirmField.getText();

            if (
                    oldPass.isBlank()
                            ||
                            newPass.isBlank()
            ) {

                statusLabel.setText(
                        "All fields required"
                );

                return;
            }

            if (!newPass.equals(confirmPass)) {

                statusLabel.setText(
                        "Password mismatch"
                );

                return;
            }

            oldPassword =
                    oldPass;

            newPassword =
                    newPass;

            stage.close();
        });

        // =========================
        // ADD
        // =========================

        root.getChildren().addAll(

                oldField,

                newField,

                confirmField,

                confirmButton,

                statusLabel
        );

        Scene scene =
                new Scene(
                        root,
                        400,
                        280
                );

        stage.setScene(scene);

        stage.showAndWait();

        if (
                oldPassword == null
                        ||
                        newPassword == null
        ) {

            return null;
        }

        return new Result(
                oldPassword,
                newPassword
        );
    }

    // =========================
    // RESULT
    // =========================

    public record Result(

            String oldPassword,

            String newPassword
    ) {
    }
}