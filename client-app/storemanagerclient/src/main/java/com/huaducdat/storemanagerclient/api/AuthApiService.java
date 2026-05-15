package com.huaducdat.storemanagerclient.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huaducdat.storemanagerclient.model.LoginRequest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthApiService {

    private static final String API =
            "http://localhost:8080/api/auth/login";

    private final ObjectMapper mapper =
            new ObjectMapper();

    // =========================
    // LOGIN
    // =========================

    public String login(
            String username,
            String password
    ) throws Exception {

        URL url =
                new URL(API);

        HttpURLConnection conn =
                (HttpURLConnection)
                        url.openConnection();

        conn.setRequestMethod("POST");

        conn.setRequestProperty(
                "Content-Type",
                "application/json"
        );

        conn.setDoOutput(true);

        // =========================
        // BODY
        // =========================

        LoginRequest request =
                LoginRequest.builder()

                        .username(username)

                        .password(password)

                        .build();

        String json =
                mapper.writeValueAsString(
                        request
                );

        OutputStream os =
                conn.getOutputStream();

        os.write(
                json.getBytes(
                        StandardCharsets.UTF_8
                )
        );

        os.flush();

        os.close();

        // =========================
        // RESPONSE
        // =========================

        if (conn.getResponseCode() != 200) {

            throw new RuntimeException(
                    "Login failed"
            );
        }

        JsonNode root =
                mapper.readTree(
                        conn.getInputStream()
                );

        return root
                .get("data")
                .get("token")
                .asText();
    }
}