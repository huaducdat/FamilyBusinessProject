package com.huaducdat.storemanagerclient.shared.navigation;

public enum Route {

    LOGIN(
            "/view/auth/login/LoginView.fxml",
            "Store Manager"
    ),
    DASHBOARD(
            "/view/dashboard/DashboardView.fxml",
            "Store Manager Dashboard"
    );

    private final String fxmlPath;

    private final String title;

    Route(
            String fxmlPath,
            String title
    ) {

        this.fxmlPath = fxmlPath;
        this.title = title;
    }

    public String getFxmlPath() {

        return fxmlPath;
    }

    public String getTitle() {

        return title;
    }
}
