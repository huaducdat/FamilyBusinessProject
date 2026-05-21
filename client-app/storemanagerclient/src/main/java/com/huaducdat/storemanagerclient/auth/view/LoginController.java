package com.huaducdat.storemanagerclient.auth.view;

import com.huaducdat.storemanagerclient.auth.presenter.LoginPresenter;
import com.huaducdat.storemanagerclient.auth.viewmodel.LoginViewModel;
import com.huaducdat.storemanagerclient.shared.ui.BackgroundEnvironmentService;
import com.huaducdat.storemanagerclient.shared.ui.BackgroundMode;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.layout.Region;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button loginButton;

    @FXML
    private ProgressIndicator loadingIndicator;

    @FXML
    private StackPane rootStack;

    @FXML
    private StackPane backgroundLayer;

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

    private final LoginViewModel viewModel;

    private final LoginPresenter presenter;

    private final BackgroundEnvironmentService backgroundEnvironmentService =
            new BackgroundEnvironmentService();

    private BackgroundMode appliedBackgroundMode;

    private final StringBuilder passwordBuffer =
            new StringBuilder();

    private boolean updatingPasswordField;

    public LoginController(
            LoginViewModel viewModel,
            LoginPresenter presenter
    ) {

        this.viewModel = viewModel;
        this.presenter = presenter;
    }

    @FXML
    public void initialize() {

        System.out.println(
                "[LoginController] initialize()"
        );

        installCapsuleInteractions();
        compactCapsule(modeCapsule);

        rootStack.sceneProperty().addListener(
                (observable, oldScene, newScene) -> {
                    if (newScene == null) {
                        backgroundEnvironmentService.dispose();
                        appliedBackgroundMode = null;
                        passwordBuffer.setLength(0);
                        viewModel.clearCredentials();
                        System.out.println(
                                "[LoginController] Scene detached; cleared password buffer"
                        );
                    } else {
                        Platform.runLater(
                                this::bootstrapBackgroundState
                        );
                    }
                }
        );

        if (rootStack.getScene() != null) {
            Platform.runLater(
                    this::bootstrapBackgroundState
            );
        }

        installPasswordHardening();
        resetLoginFields();

        usernameField.textProperty().bindBidirectional(
                viewModel.usernameProperty()
        );

        statusLabel.textProperty().bind(
                viewModel.errorMessageProperty()
        );
        statusLabel.visibleProperty().bind(
                Bindings.isNotEmpty(viewModel.errorMessageProperty())
        );
        statusLabel.managedProperty().bind(
                statusLabel.visibleProperty()
        );

        loginButton.disableProperty().bind(
                viewModel.loadingProperty()
        );

        loadingIndicator.visibleProperty().bind(
                viewModel.loadingProperty()
        );
        loadingIndicator.managedProperty().bind(
                loadingIndicator.visibleProperty()
        );
    }

    @FXML
    public void onLogin() {

        syncPasswordBufferToViewModel();
        presenter.login();
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

    private void resetLoginFields() {

        viewModel.clearCredentials();
        passwordBuffer.setLength(0);

        usernameField.setPromptText("");
        usernameField.clear();
        usernameField.setText("");

        updatingPasswordField = true;
        try {
            passwordField.clear();
            passwordField.setText("");
            passwordField.setPromptText("Password");
        } finally {
            updatingPasswordField = false;
        }
        renderMaskedPassword();

        System.out.println(
                "[LoginController] Reset login fields -> username='"
                        + usernameField.getText()
                        + "', passwordBufferLength="
                        + passwordBuffer.length()
        );
    }

    private void installPasswordHardening() {

        passwordField.setText("");
        passwordField.setPromptText("Password");
        passwordField.addEventFilter(
                KeyEvent.KEY_TYPED,
                this::handlePasswordKeyTyped
        );
        passwordField.addEventFilter(
                KeyEvent.KEY_PRESSED,
                this::handlePasswordKeyPressed
        );
        passwordField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (updatingPasswordField) {
                        return;
                    }

                    String safeValue =
                            newValue == null ? "" : newValue;
                    String expectedMask =
                            "•".repeat(passwordBuffer.length());

                    if (!safeValue.equals(expectedMask)) {
                        System.out.println(
                                "[LoginController] Suspicious password text change detected; restoring masked buffer"
                        );
                        Platform.runLater(
                                this::renderMaskedPassword
                        );
                    }
                }
        );
        passwordField.focusedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        renderMaskedPassword();
                    }
                }
        );
        System.out.println(
                "[LoginController] Password hardening installed"
        );
    }

    private void handlePasswordKeyTyped(
            KeyEvent event
    ) {

        String character = event.getCharacter();

        if (character == null || character.isEmpty()) {
            return;
        }

        if (character.charAt(0) < 32) {
            return;
        }

        passwordBuffer.append(character);
        event.consume();
        syncPasswordBufferToViewModel();
        renderMaskedPassword();
    }

    private void handlePasswordKeyPressed(
            KeyEvent event
    ) {

        if (event.getCode() == KeyCode.BACK_SPACE) {
            if (passwordBuffer.length() > 0) {
                passwordBuffer.deleteCharAt(
                        passwordBuffer.length() - 1
                );
                event.consume();
                syncPasswordBufferToViewModel();
                renderMaskedPassword();
            }
            return;
        }

        if (event.getCode() == KeyCode.DELETE) {
            if (passwordBuffer.length() > 0) {
                passwordBuffer.setLength(0);
                event.consume();
                syncPasswordBufferToViewModel();
                renderMaskedPassword();
            }
            return;
        }

        if (event.isShortcutDown()
                && event.getCode() == KeyCode.V) {
            String clipboardText =
                    Clipboard.getSystemClipboard()
                            .getString();

            if (clipboardText != null
                    && !clipboardText.isEmpty()) {
                passwordBuffer.append(clipboardText);
                event.consume();
                syncPasswordBufferToViewModel();
                renderMaskedPassword();
            }
        }
    }

    private void syncPasswordBufferToViewModel() {

        String password = passwordBuffer.toString();

        viewModel.setPassword(password);

        System.out.println(
                "[LoginController] Password buffer updated -> length="
                        + passwordBuffer.length()
        );
    }

    private void renderMaskedPassword() {

        String masked = "•".repeat(passwordBuffer.length());

        updatingPasswordField = true;
        try {
            passwordField.setText(masked);
            passwordField.positionCaret(masked.length());
            System.out.println(
                    "[LoginController] Password field rendered -> visibleLength="
                            + masked.length()
            );
        } finally {
            updatingPasswordField = false;
        }
    }

    private void bootstrapBackgroundState() {

        if (rootStack.getScene() == null) {
            return;
        }

        resetLoginFields();

        BackgroundMode backgroundMode =
                backgroundEnvironmentService.loadMode();

        syncModePills(backgroundMode);
        applyMode(backgroundMode);

        Platform.runLater(this::resetLoginFields);
    }
}
