package com.huaducdat.storemanagerclient.shared.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class BaseViewModel {

    private final BooleanProperty loading =
            new SimpleBooleanProperty(false);

    private final StringProperty errorMessage =
            new SimpleStringProperty("");

    public BooleanProperty loadingProperty() {

        return loading;
    }

    public boolean isLoading() {

        return loading.get();
    }

    public void setLoading(
            boolean value
    ) {

        loading.set(value);
    }

    public StringProperty errorMessageProperty() {

        return errorMessage;
    }

    public String getErrorMessage() {

        return errorMessage.get();
    }

    public void setErrorMessage(
            String message
    ) {

        errorMessage.set(
                message == null ? "" : message
        );
    }

    public void clearError() {

        setErrorMessage("");
    }
}
