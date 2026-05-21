package com.huaducdat.storemanagerclient.shared.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Objects;
import java.net.URL;
import java.util.List;

public class NavigationService {

    private Stage primaryStage;

    private Callback<Class<?>, Object> controllerFactory;

    public void setPrimaryStage(
            Stage primaryStage
    ) {

        this.primaryStage = primaryStage;
    }

    public void setControllerFactory(
            Callback<Class<?>, Object> controllerFactory
    ) {

        this.controllerFactory = controllerFactory;
    }

    public void start(
            Stage stage,
            Route route
    ) throws Exception {

        log(
                "Starting navigation with route: "
                        + route
                        + " -> "
                        + route.getFxmlPath()
        );

        setPrimaryStage(stage);
        navigate(route);
        primaryStage.show();
    }

    public void navigateToLogin() {

        navigateTo(Route.LOGIN);
    }

    public void navigateToDashboard() {

        navigateTo(Route.DASHBOARD);
    }

    public void navigateTo(
            Route route
    ) {

        try {
            navigate(route);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void navigate(
            Route route
    ) throws Exception {

        if (primaryStage == null) {
            throw new IllegalStateException(
                    "Primary stage has not been initialized"
            );
        }

        log(
                "Loading route: "
                        + route
                        + " from "
                        + route.getFxmlPath()
        );

        URL fxmlResource = resolveResource(route);

        FXMLLoader loader =
                new FXMLLoader(fxmlResource);

        if (controllerFactory != null) {
            log("Using custom controller factory for route: " + route);
            loader.setControllerFactory(controllerFactory);
        }

        try {
            log("Calling FXMLLoader.load() for route: " + route);

            Parent root = loader.load();

            log(
                    "Loaded root: "
                            + root.getClass().getName()
            );
            log(
                    "Root stylesheets: "
                            + root.getStylesheets()
            );

            Scene scene =
                    new Scene(
                            root
                    );

            applyThemeStylesheet(scene);

            log(
                    "Scene stylesheets: "
                            + scene.getStylesheets()
            );

            Object controller = loader.getController();
            log(
                    "Loaded FXML: "
                            + route.getFxmlPath()
                            + ", controller="
                            + (controller == null
                            ? "null"
                            : controller.getClass().getName())
            );
            log(
                    "FXML loader location: "
                            + loader.getLocation()
            );

            primaryStage.setTitle(
                    route.getTitle()
            );

            primaryStage.setScene(scene);
        } catch (Exception ex) {
            log(
                    "FXMLLoader.load() failed for route: "
                            + route
                            + " -> "
                            + ex.getClass().getName()
                            + ": "
                            + ex.getMessage()
            );
            ex.printStackTrace();
            throw ex;
        }
    }

    private URL resolveResource(
            Route route
    ) {

        URL resource =
                NavigationService.class.getResource(
                        route.getFxmlPath()
                );

        if (resource == null) {
            log(
                    "Failed to resolve FXML resource: "
                            + route.getFxmlPath()
            );
            throw new IllegalStateException(
                    "Unable to load FXML: " + route.getFxmlPath()
            );
        }

        log(
                "Resolved FXML resource: "
                        + Objects.requireNonNull(resource).toExternalForm()
        );

        return resource;
    }

    private void log(
            String message
    ) {

        System.out.println(
                "[NavigationService] " + message
        );
    }

    private void applyThemeStylesheet(
            Scene scene
    ) {

        URL themeResource =
                NavigationService.class.getResource(
                        "/theme/app-theme.css"
                );

        if (themeResource == null) {
            log("Theme stylesheet could not be resolved");
            return;
        }

        String themeUrl =
                themeResource.toExternalForm();

        List<String> stylesheets =
                scene.getStylesheets();

        stylesheets.removeIf(
                stylesheet -> stylesheet.endsWith("/theme/app-theme.css")
                        || stylesheet.endsWith("theme/app-theme.css")
        );

        stylesheets.add(themeUrl);

        log(
                "Applied theme stylesheet: "
                        + themeUrl
        );
    }
}
