package com.huaducdat.storemanagerclient.shared.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaducdat.storemanagerclient.shared.config.ConfigService;
import com.huaducdat.storemanagerclient.shared.session.SessionService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private final ObjectMapper mapper =
            new ObjectMapper();

    private final ConfigService configService;

    private final SessionService sessionService;

    public ApiClient(
            ConfigService configService,
            SessionService sessionService
    ) {

        this.configService = configService;
        this.sessionService = sessionService;
    }

    public <T> ApiResponse<T> get(
            String path,
            Class<T> responseType
    ) {

        return request("GET", path, null, responseType);
    }

    public <T> ApiResponse<T> post(
            String path,
            Object body,
            Class<T> responseType
    ) {

        return request("POST", path, body, responseType);
    }

    public <T> ApiResponse<T> patch(
            String path,
            Object body,
            Class<T> responseType
    ) {

        return request("PATCH", path, body, responseType);
    }

    private <T> ApiResponse<T> request(
            String method,
            String path,
            Object body,
            Class<T> responseType
    ) {

        HttpURLConnection connection = null;

        try {

            connection =
                    openConnection(
                            method,
                            path
                    );

            if (body != null) {

                writeBody(
                        connection,
                        body
                );
            }

            int statusCode =
                    connection.getResponseCode();

            InputStream stream =
                    statusCode >= 200 && statusCode < 300
                            ? connection.getInputStream()
                            : connection.getErrorStream();

            String rawBody =
                    readBody(stream);

            if (statusCode == 401) {

                throw new ApiException(
                        statusCode,
                        extractMessage(rawBody, "Authentication failed"),
                        rawBody,
                        true
                );
            }

            if (statusCode < 200 || statusCode >= 300) {

                throw new ApiException(
                        statusCode,
                        extractMessage(rawBody, "Request failed"),
                        rawBody,
                        false
                );
            }

            JsonNode root =
                    rawBody == null || rawBody.isBlank()
                            ? mapper.createObjectNode()
                            : mapper.readTree(rawBody);

            JsonNode payload =
                    extractPayload(root);

            T data =
                    responseType == Void.class
                            ? null
                            : mapper.treeToValue(payload, responseType);

            return new ApiResponse<>(
                    statusCode,
                    extractMessage(rawBody, null),
                    data
            );

        } catch (IOException ex) {

            throw new ApiException(
                    -1,
                    ex.getMessage(),
                    null,
                    false
            );
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection openConnection(
            String method,
            String path
    ) throws IOException {

        URL url =
                new URL(
                        joinUrl(
                                configService.getApiBaseUrl(),
                                path
                        )
                );

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method);
        connection.setConnectTimeout(
                configService.getConnectTimeoutMillis()
        );
        connection.setReadTimeout(
                configService.getReadTimeoutMillis()
        );
        connection.setRequestProperty(
                "Accept",
                "application/json"
        );
        connection.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        String token =
                sessionService.getToken();

        if (token != null && !token.isBlank()) {

            connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + token
            );
        }

        return connection;
    }

    private void writeBody(
            HttpURLConnection connection,
            Object body
    ) throws IOException {

        connection.setDoOutput(true);

        byte[] payload =
                mapper.writeValueAsBytes(body);

        try (OutputStream outputStream =
                     connection.getOutputStream()) {

            outputStream.write(payload);
        }
    }

    private String readBody(
            InputStream stream
    ) throws IOException {

        if (stream == null) {
            return "";
        }

        try (InputStream inputStream = stream) {

            return new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }
    }

    private JsonNode extractPayload(
            JsonNode root
    ) {

        if (root == null) {
            return mapper.createObjectNode();
        }

        JsonNode dataNode =
                root.get("data");

        return dataNode == null || dataNode.isNull()
                ? root
                : dataNode;
    }

    private String extractMessage(
            String rawBody,
            String fallback
    ) {

        if (rawBody == null || rawBody.isBlank()) {
            return fallback;
        }

        try {

            JsonNode root =
                    mapper.readTree(rawBody);

            JsonNode messageNode =
                    root.get("message");

            if (messageNode != null && !messageNode.isNull()) {
                return messageNode.asText();
            }

            JsonNode errorNode =
                    root.get("error");

            if (errorNode != null && !errorNode.isNull()) {
                return errorNode.asText();
            }

            JsonNode detailNode =
                    root.get("detail");

            if (detailNode != null && !detailNode.isNull()) {
                return detailNode.asText();
            }

        } catch (Exception ignored) {
            // Fall back to provided default message.
        }

        return fallback != null ? fallback : rawBody;
    }

    private String joinUrl(
            String baseUrl,
            String path
    ) {

        String normalizedBase =
                baseUrl.endsWith("/")
                        ? baseUrl.substring(0, baseUrl.length() - 1)
                        : baseUrl;

        String normalizedPath =
                path.startsWith("/")
                        ? path
                        : "/" + path;

        return normalizedBase + normalizedPath;
    }
}
