package com.huaducdat.storemanagerclient.dashboard.view;

import com.huaducdat.storemanagerclient.auth.service.AuthService;
import com.huaducdat.storemanagerclient.shared.navigation.NavigationService;
import com.huaducdat.storemanagerclient.shared.ui.BackgroundEnvironmentService;
import com.huaducdat.storemanagerclient.shared.ui.BackgroundMode;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;

public class DashboardController {

    @FXML
    private StackPane rootStack;

    @FXML
    private StackPane backgroundLayer;

    @FXML
    private StackPane foregroundLayer;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private MediaView backgroundVideoView;

    @FXML
    private StackPane videoModePill;

    @FXML
    private StackPane imageModePill;

    @FXML
    private HBox modeCapsule;

    @FXML
    private BorderPane dashboardRoot;

    @FXML
    private StackPane topbarPanel;

    @FXML
    private StackPane sidebarPanel;

    @FXML
    private StackPane contentPanel;

    private final AuthService authService;

    private final NavigationService navigationService;

    private final BackgroundEnvironmentService backgroundEnvironmentService =
            new BackgroundEnvironmentService();

    private BackgroundMode appliedBackgroundMode;

    public DashboardController(
            AuthService authService,
            NavigationService navigationService
    ) {

        this.authService = authService;
        this.navigationService = navigationService;

        System.out.println(
                "[DashboardController] Constructed"
        );
    }

    @FXML
    public void initialize() {
        installCapsuleInteractions();
        compactCapsule(modeCapsule);

        Platform.runLater(this::forceLayerOrder);

        rootStack.sceneProperty().addListener(
                (observable, oldScene, newScene) -> {
                    if (newScene == null) {
                        backgroundEnvironmentService.dispose();
                        appliedBackgroundMode = null;
                    } else {
                        Platform.runLater(() -> {
                            forceLayerOrder();
                            bootstrapBackgroundState();
                        });
                    }
                }
        );

        if (rootStack.getScene() != null) {
            Platform.runLater(() -> {
                forceLayerOrder();
                bootstrapBackgroundState();
            });
        }
    }

    @FXML
    public void onLogout() {

        System.out.println(
                "[DashboardController] Logout requested"
        );

        backgroundEnvironmentService.dispose();

        authService.logout()
                .whenComplete((ignored, throwable) -> Platform.runLater(() -> {

                    if (throwable != null) {
                        throwable.printStackTrace();
                    }

                    System.out.println(
                            "[DashboardController] Session cleared, returning to login"
                    );

                    navigationService.navigateToLogin();
                }));
    }

    @FXML
    public void onVideoModeSelected() {

        applyMode(BackgroundMode.VIDEO);
    }

    @FXML
    public void onImageModeSelected() {

        applyMode(BackgroundMode.IMAGE);
    }

    private void applyMode(
            BackgroundMode mode
    ) {

        if (mode == appliedBackgroundMode) {
            return;
        }

        appliedBackgroundMode = mode;
        syncModePills(mode);

        backgroundEnvironmentService.applyMode(
                mode,
                backgroundImageView,
                backgroundVideoView,
                backgroundLayer
        );

        Platform.runLater(
                this::forceLayerOrder
        );
    }

    private void syncModePills(
            BackgroundMode mode
    ) {

        setPillState(
                videoModePill,
                mode == BackgroundMode.VIDEO
        );
        setPillState(
                imageModePill,
                mode == BackgroundMode.IMAGE
        );
    }

    private void bootstrapBackgroundState() {

        if (rootStack.getScene() == null) {
            return;
        }

        BackgroundMode backgroundMode =
                backgroundEnvironmentService.loadMode();

        syncModePills(backgroundMode);
        applyMode(backgroundMode);
    }

    private void forceLayerOrder() {

        if (backgroundLayer != null) {
            backgroundLayer.toBack();
        }

        if (foregroundLayer != null) {
            foregroundLayer.toFront();
        }

        if (dashboardRoot != null) {
            dashboardRoot.requestLayout();
        }
    }

    private void installCapsuleInteractions() {

        installPill(
                videoModePill,
                BackgroundMode.VIDEO
        );
        installPill(
                imageModePill,
                BackgroundMode.IMAGE
        );
    }

    private void installPill(
            StackPane pill,
            BackgroundMode mode
    ) {

        pill.setCursor(Cursor.HAND);
        pill.setOnMouseClicked(
                event -> applyMode(mode)
        );
        pill.setOnMouseEntered(
                event -> {
                    if (mode != appliedBackgroundMode) {
                        pill.setTranslateY(-1);
                    }
                }
        );
        pill.setOnMouseExited(
                event -> syncModePills(appliedBackgroundMode)
        );
    }

    private void compactCapsule(
            HBox capsule
    ) {

        capsule.setFillHeight(false);
        capsule.setMaxWidth(Region.USE_PREF_SIZE);
        capsule.setMinWidth(Region.USE_PREF_SIZE);
        capsule.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }

    private void setPillState(
            StackPane pill,
            boolean active
    ) {

        pill.getStyleClass().removeAll(
                "mode-pill-active",
                "mode-pill-idle"
        );
        pill.getStyleClass().add(
                active ? "mode-pill-active" : "mode-pill-idle"
        );
        pill.setTranslateY(active ? -2 : 0);
        pill.setScaleX(active ? 1.0 : 0.985);
        pill.setScaleY(active ? 1.0 : 0.985);
    }
}
