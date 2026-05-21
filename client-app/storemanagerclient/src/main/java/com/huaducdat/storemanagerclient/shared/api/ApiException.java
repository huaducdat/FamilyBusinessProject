package com.huaducdat.storemanagerclient.shared.api;

public class ApiException extends RuntimeException {

    private final int statusCode;

    private final String responseBody;

    private final boolean authenticationFailure;

    public ApiException(
            int statusCode,
            String message,
            String responseBody,
            boolean authenticationFailure
    ) {

        super(message);

        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.authenticationFailure = authenticationFailure;
    }

    public int getStatusCode() {

        return statusCode;
    }

    public String getResponseBody() {

        return responseBody;
    }

    public boolean isAuthenticationFailure() {

        return authenticationFailure;
    }
}
