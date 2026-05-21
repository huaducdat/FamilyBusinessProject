package com.huaducdat.storemanagerclient.shared.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AppConfig {

    @Builder.Default
    private String apiBaseUrl = "http://localhost:8080";

    @Builder.Default
    private int connectTimeoutMillis = 10_000;

    @Builder.Default
    private int readTimeoutMillis = 15_000;
}
