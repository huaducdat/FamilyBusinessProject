package com.huaducdat.storemanagerclient.auth.viewmodel;

import com.huaducdat.storemanagerclient.shared.ui.BaseViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel extends BaseViewModel {

    private final StringProperty username =
            new SimpleStringProperty("");

    private final StringProperty password =
            new SimpleStringProperty("");

    public StringProperty usernameProperty() {

        return username;
    }

    public String getUsername() {

        return username.get();
    }

    public void setUsername(
            String value
    ) {

        username.set(value);
    }

    public StringProperty passwordProperty() {

        return password;
    }

    public String getPassword() {

        return password.get();
    }

    public void setPassword(
            String value
    ) {

        password.set(value);
    }

    public void clearCredentials() {

        username.set("");
        password.set("");
    }
}
