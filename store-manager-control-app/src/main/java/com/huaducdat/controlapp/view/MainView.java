package com.huaducdat.controlapp.view;

import com.huaducdat.controlapp.presenter.MainPresenter;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainView {

    private MainPresenter presenter;

    // =========================
    // UI
    // =========================

    private final VBox root;

    private final Label statusLabel;

    private final Button startButton;

    private final Button stopButton;

    private final Button backupButton;

    private final Button restoreButton;

    private final Button changePasswordButton;

    // =========================
    // CONSTRUCTOR
    // =========================

    public MainView() {

        root =
                new VBox();

        root.setSpacing(20);

        root.setPadding(
                new Insets(20)
        );

        // =========================
        // STATUS
        // =========================

        statusLabel =
                new Label(
                        "Backend Status: STOPPED"
                );

        // =========================
        // BUTTONS
        // =========================

        startButton =
                new Button(
                        "Start Backend"
                );

        stopButton =
                new Button(
                        "Stop Backend"
                );

        backupButton =
                new Button(
                        "Backup Database"
                );


        restoreButton =
                new Button(
                        "Restore Database"
                );

        changePasswordButton =
                new Button(
                        "Change Technical Password"
                );

        // =========================
        // ACTIONS
        // =========================

        startButton.setOnAction(e -> {

            if (presenter != null) {

                presenter.onStartBackend();
            }
        });

        stopButton.setOnAction(e -> {

            if (presenter != null) {

                presenter.onStopBackend();
            }
        });

        backupButton.setOnAction(e -> {

            if (presenter != null) {

                presenter.onBackupDatabase();
            }
        });

        restoreButton.setOnAction(e -> {

            if (presenter != null) {

                presenter.onRestoreDatabase();
            }
        });

        changePasswordButton.setOnAction(e -> {

            if (presenter != null) {

                presenter.onChangePassword();
            }
        });

        // =========================
        // ADD
        // =========================

        root.getChildren().addAll(

                statusLabel,

                startButton,

                stopButton,

                backupButton,

                restoreButton,

                changePasswordButton
        );
    }


    // =========================
    // SET PRESENTER
    // =========================

    public void setPresenter(
            MainPresenter presenter
    ) {

        this.presenter =
                presenter;
    }

    // =========================
    // UPDATE STATUS
    // =========================

    public void updateStatus(
            String text
    ) {

        statusLabel.setText(
                text
        );
    }

    // =========================
    // ROOT
    // =========================

    public Parent getRoot() {

        return root;
    }
}