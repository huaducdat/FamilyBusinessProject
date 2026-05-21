package com.huaducdat.storemanagerclient.shared.api;

public class ApiResponse<T> {

    private final int statusCode;

    private final String message;

    private final T data;

    public ApiResponse(
            int statusCode,
            String message,
            T data
    ) {

        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public String getMessage() {

        return message;
    }

    public T getData() {

        return data;
    }

    public boolean isSuccessful() {

        return statusCode >= 200 && statusCode < 300;
    }
}
